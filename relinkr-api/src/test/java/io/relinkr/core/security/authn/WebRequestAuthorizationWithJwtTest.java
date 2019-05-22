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

package io.relinkr.core.security.authn;

import static io.relinkr.core.security.authn.jwt.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static io.relinkr.core.security.authn.jwt.JwtAuthenticationFilter.BEARER_TOKEN_PREFIX;
import static io.relinkr.test.Mocks.JWT_TOKEN_EXPIRED;
import static io.relinkr.test.Mocks.JWT_TOKEN_VALID;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import org.junit.Test;

public class WebRequestAuthorizationWithJwtTest extends AbstractWebRequestAuthorizationTest {

  @Test
  public void givenInvalidAuthentication_whenRootAccessed_thenOk() throws Exception {
    super.givenInvalidAuthentication_whenRootAccessed_thenStatus(SC_OK);
  }

  @Test
  public void givenInvalidAuthentication_whenShortLinkAccessed_thenOk()
      throws Exception {

    super.givenInvalidAuthentication_whenShortLinkAccessed_thenStatus(SC_OK);
  }

  @Test
  public void givenInvalidAuthentication_whenApiAccessed_thenUnauthorized() throws Exception {
    super.givenInvalidAuthentication_whenApiAccessed_thenStatus(SC_UNAUTHORIZED);
  }

  @Override
  protected void withValidAuthentication() {
    setAuthorizationHeader(JWT_TOKEN_VALID);
  }

  @Override
  protected void withInvalidAuthentication() {
    setAuthorizationHeader(JWT_TOKEN_EXPIRED);
  }

  private void setAuthorizationHeader(String value) {
    addHttpHeader(AUTHORIZATION_HEADER, BEARER_TOKEN_PREFIX + " " + value);
  }

}
