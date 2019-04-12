package com.springuni.hermes.core.security.authn.jwt;

import io.jsonwebtoken.lang.Assert;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by lcsontos on 5/17/17.
 */
public interface JwtAuthenticationService extends AuthenticationProvider {

    String createJwtToken(Authentication authentication, int minutes);

    Authentication parseJwtToken(String jwtToken) throws AuthenticationException;

    @Override
    default Authentication authenticate(Authentication authentication) {
        Assert.isInstanceOf(JwtAuthenticationToken.class, authentication);
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        return parseJwtToken(jwtAuthenticationToken.getPrincipal());
    }

    @Override
    default boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
