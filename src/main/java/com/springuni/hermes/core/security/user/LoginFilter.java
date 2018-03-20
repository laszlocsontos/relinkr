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

package com.springuni.hermes.core.security.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Processes login requests and delegates deciding the decision to {@link AuthenticationManager}.
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    /**
     * Create a login filter.
     *
     * @param filterProcessesUrl filter's URL
     * @param objectMapper a ObjectMapper instance
     */
    public LoginFilter(String filterProcessesUrl, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(filterProcessesUrl, "POST"));
        this.objectMapper = objectMapper;
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        LoginRequest loginRequest = obtainLoginRequest(request);
        Authentication authRequest = createAuthenticationRequest(loginRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }

    private Authentication createAuthenticationRequest(LoginRequest loginRequest) {
        String username = Optional
                .ofNullable(loginRequest.getUsername())
                .map(String::trim)
                .map(String::toLowerCase)
                .orElse("");

        String password = Optional
                .ofNullable(loginRequest.getPassword())
                .orElse("");

        return new UsernamePasswordAuthenticationToken(username, password);
    }

    private LoginRequest obtainLoginRequest(HttpServletRequest request)
            throws AuthenticationException {

        try {
            return objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException(
                    "Authentication payload not supported: " + e.getMessage(), e);
        }
    }

}
