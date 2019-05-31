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

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

/**
 * Wrapper layer around the actual JWT implementation; also acts as an authentication strategy by
 * extending {@link AuthenticationProvider}.
 */
public interface JwtAuthenticationService extends AuthenticationProvider {

  /**
   * Creates a JWT token from the given {@link Authentication} and that will be valid for
   * {@code minutes} minutes.
   *
   * @param authentication Authentication
   * @param minutes validation time in minutes
   * @return String representation of the created JWT token
   * @throws AuthenticationException When creating the JWT token was not possible
   */
  String createJwtToken(Authentication authentication, int minutes) throws AuthenticationException;

  /**
   * Parses the string representation of a JWT token and converts it to an {@link Authentication}.
   * @param jwtToken JWT token to parse
   * @return Authentication
   * @throws AuthenticationException when parsing the JWT token was not possible
   */
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
