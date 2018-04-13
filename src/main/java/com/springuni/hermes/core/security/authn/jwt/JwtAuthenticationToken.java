/**
 * Copyright (c) 2017-present Laszlo Csontos All rights reserved.
 *
 * This file is part of springuni-particles.
 *
 * springuni-particles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * springuni-particles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with springuni-particles.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.springuni.hermes.core.security.authn.jwt;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
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
     * @param claims JWT claims
     * @return a JwtAuthenticationToken
     */
    static JwtAuthenticationToken of(Claims claims) {
        long userId = 0;
        try {
            userId = Long.valueOf(claims.getSubject());
        } catch (NumberFormatException e) {
            throw new MalformedJwtException(e.getMessage(), e);
        }

        Collection<? extends GrantedAuthority> authorities =
                Optional.ofNullable(claims.get(AUTHORITIES))
                        .map(String::valueOf)
                        .map(it -> it.split(","))
                        .map(Arrays::stream)
                        .map(it -> it.map(String::trim).map(String::toUpperCase))
                        .map(it -> it.map(SimpleGrantedAuthority::new))
                        .map(it -> it.collect(toSet()))
                        .orElse(emptySet());

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userId,
                authorities);

        Instant now = Instant.now();
        Instant expiration = claims.getExpiration().toInstant();

        Instant notBefore = Optional.ofNullable(claims.getNotBefore())
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
