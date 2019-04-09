package com.springuni.hermes.core.security.authn.jwt;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import com.nimbusds.jwt.JWTClaimsSet;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Created by lcsontos on 5/17/17.
 */
class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final String AUTHORITIES = "authorities";

    private final long userId;

    private JwtAuthenticationToken(
            long userId, Collection<? extends GrantedAuthority> authorities) {

        super(authorities);
        this.userId = userId;
    }

    /**
     * Factory method for creating a new {@code {@link JwtAuthenticationToken}}.
     *
     * @param claimsSet JWT claims
     * @return a JwtAuthenticationToken
     */
    static JwtAuthenticationToken of(JWTClaimsSet claimsSet) {
        long userId;
        try {
            userId = Long.valueOf(claimsSet.getSubject());
        } catch (NumberFormatException e) {
            throw new JwtTokenException(e.getMessage(), e);
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

        JwtAuthenticationToken jwtAuthenticationToken =
                new JwtAuthenticationToken(userId, authorities);

        Instant now = Instant.now();
        Instant expiration = claimsSet.getExpirationTime().toInstant();

        Instant notBefore = Optional.ofNullable(claimsSet.getNotBeforeTime())
                .map(Date::toInstant)
                .orElse(now);

        jwtAuthenticationToken.setAuthenticated(
                now.compareTo(notBefore) >= 0 && now.isBefore(expiration)
        );

        return jwtAuthenticationToken;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Long getPrincipal() {
        return userId;
    }

}
