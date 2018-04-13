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

package com.springuni.hermes.core.security.authn.signin;

import com.springuni.hermes.core.security.authn.jwt.JwtTokenService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Created by lcsontos on 5/17/17.
 */
@Slf4j
public class SignInSuccessHandler implements AuthenticationSuccessHandler {

    static final int ONE_DAY_MINUTES = 24 * 60;
    static final String X_SET_AUTHORIZATION_BEARER_HEADER = "X-Set-Authorization-Bearer";

    private final JwtTokenService jwtTokenService;

    public SignInSuccessHandler(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {

        if (response.containsHeader(X_SET_AUTHORIZATION_BEARER_HEADER)) {
            log.debug("{} has already been set.", X_SET_AUTHORIZATION_BEARER_HEADER);
            return;
        }

        String jwtToken = jwtTokenService.createJwtToken(authentication, ONE_DAY_MINUTES);
        response.setHeader(X_SET_AUTHORIZATION_BEARER_HEADER, jwtToken);
    }

}
