package com.springuni.hermes.core.security.authn.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by lcsontos on 5/17/17.
 */
public interface JwtTokenService {

    String createJwtToken(Authentication authentication, int minutes);

    Authentication parseJwtToken(String jwtToken) throws AuthenticationException;

}
