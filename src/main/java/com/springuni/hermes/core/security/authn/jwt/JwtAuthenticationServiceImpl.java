package com.springuni.hermes.core.security.authn.jwt;

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
import com.springuni.hermes.core.security.authn.user.UserIdAuthenticationToken;
import com.springuni.hermes.core.util.IdentityGenerator;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.NonceExpiredException;

/**
 * Created by lcsontos on 5/17/17.
 */
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {

    private static final String AUTHORITIES = "authorities";

    private final JWSSigner signer;
    private final JWSVerifier verifier;

    private final IdentityGenerator identityGenerator;

    public JwtAuthenticationServiceImpl(
            PrivateKey privateKey, PublicKey publicKey, IdentityGenerator identityGenerator) {

        this.verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
        this.signer = new RSASSASigner(privateKey);
        this.identityGenerator = identityGenerator;
    }

    @Override
    public String createJwtToken(Authentication authentication, int minutes) {
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

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(RS256).build(), claimsSet);

        // Compute the RSA signature
        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }

        return signedJWT.serialize();
    }

    @Override
    public Authentication parseJwtToken(String jwtToken) throws AuthenticationException {
        JWTClaimsSet claimsSet;
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwtToken);

            if (!signedJWT.verify(verifier)) {
                throw new BadCredentialsException("Token verification failed.");
            }

            claimsSet = signedJWT.getJWTClaimsSet();
        } catch (JOSEException | ParseException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }

        long userId;
        try {
            userId = Long.valueOf(claimsSet.getSubject());
        } catch (NumberFormatException e) {
            throw new BadCredentialsException(e.getMessage(), e);
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
