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
