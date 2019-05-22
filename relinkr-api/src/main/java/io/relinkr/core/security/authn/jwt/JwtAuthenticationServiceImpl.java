/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
 
package io.relinkr.core.security.authn.jwt;

import static com.nimbusds.jose.JWSAlgorithm.RS256;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.relinkr.core.security.authn.user.UserIdAuthenticationToken;
import io.relinkr.core.util.IdentityGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.util.StringUtils;

/**
 * Created by lcsontos on 5/17/17.
 */
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {

  private static final String AUTHORITIES = "authorities";

  private final JWSSigner signer;
  private final JWSVerifier verifier;

  private final IdentityGenerator identityGenerator;

  /**
   * Creates a new {@code JwtAuthenticationService}.
   *
   * @param privateKey RSA private key for signing JWT tokens
   * @param publicKey RSA public key for verifying JWT tokens' signature
   * @param identityGenerator ID generator for assigning unique IDs to tokens
   */
  public JwtAuthenticationServiceImpl(
      PrivateKey privateKey, PublicKey publicKey, IdentityGenerator identityGenerator) {

    this.verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
    this.signer = new RSASSASigner(privateKey);
    this.identityGenerator = identityGenerator;
  }

  @Override
  public String createJwtToken(@NonNull Authentication authentication, int minutes) {
    String authorities = authentication.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .map(String::toUpperCase)
        .collect(Collectors.joining(","));

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .jwtID(String.valueOf(identityGenerator.generate()))
        .subject(authentication.getName())
        .expirationTime(new Date(currentTimeMillis() + minutes * 60 * 1000))
        .issueTime(new Date())
        .claim(AUTHORITIES, authorities)
        .build();

    SignedJWT signedJwt = new SignedJWT(new JWSHeader.Builder(RS256).build(), claimsSet);

    // Compute the RSA signature
    try {
      signedJwt.sign(signer);
    } catch (JOSEException je) {
      throw new InternalAuthenticationServiceException(je.getMessage(), je);
    }

    return signedJwt.serialize();
  }

  @Override
  public Authentication parseJwtToken(String jwtToken) throws AuthenticationException {
    if (!StringUtils.hasText(jwtToken)) {
      throw new BadCredentialsException("Null or empty token.");
    }

    JWTClaimsSet claimsSet;
    try {
      SignedJWT signedJwt = SignedJWT.parse(jwtToken);

      if (!signedJwt.verify(verifier)) {
        throw new BadCredentialsException("Token verification failed.");
      }

      claimsSet = signedJwt.getJWTClaimsSet();
    } catch (JOSEException | ParseException je) {
      throw new BadCredentialsException(je.getMessage(), je);
    } catch (Exception ex) {
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
    }

    long userId;
    try {
      userId = Long.valueOf(claimsSet.getSubject());
    } catch (NumberFormatException nfe) {
      throw new BadCredentialsException(nfe.getMessage(), nfe);
    }

    Instant expiration = Optional.ofNullable(claimsSet.getExpirationTime())
        .map(Date::toInstant)
        .orElseThrow(() -> new BadCredentialsException("Missing expiration date."));

    Instant now = Instant.now();
    if (now.isAfter(expiration)) {
      throw new NonceExpiredException("Token has expired.");
    }

    Collection<? extends GrantedAuthority> authorities =
        Optional.ofNullable(claimsSet.getClaim(AUTHORITIES))
            .map(String::valueOf)
            .map(it -> it.split(","))
            .map(Arrays::stream)
            .map(it -> it.map(String::trim).map(String::toUpperCase))
            .map(it -> it.map(SimpleGrantedAuthority::new))
            .map(it -> it.collect(toSet()))
            .orElse(emptySet());

    return UserIdAuthenticationToken.of(userId, authorities);
  }

}
