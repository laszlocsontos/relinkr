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

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Created by lcsontos on 5/17/17.
 */
class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private final String bearerToken;

  private JwtAuthenticationToken(String bearerToken) {
    super(null);
    setAuthenticated(false);
    this.bearerToken = bearerToken;
  }

  static JwtAuthenticationToken of(String bearerToken) {
    return new JwtAuthenticationToken(bearerToken);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public String getPrincipal() {
    return bearerToken;
  }

}
