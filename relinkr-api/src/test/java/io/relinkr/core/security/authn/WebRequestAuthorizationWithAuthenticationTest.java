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

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.security.core.context.SecurityContextHolder.createEmptyContext;
import static org.springframework.security.test.context.TestSecurityContextHolder.clearContext;
import static org.springframework.security.test.context.TestSecurityContextHolder.setContext;

import org.junit.After;
import org.junit.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public class WebRequestAuthorizationWithAuthenticationTest
    extends AbstractWebRequestAuthorizationTest {

  private static Authentication AUTHORIZED_AUTHENTICATION_TOKEN =
      new TestingAuthenticationToken("1", null, "ROLE_USER");
  private static Authentication UNAUTHORIZED_AUTHENTICATION_TOKEN =
      new TestingAuthenticationToken("2", null, "ROLE_NONE");

  @After
  public void tearDown() {
    clearContext();
  }

  @Test
  public void givenInvalidAuthentication_whenRootAccessed_thenOk() throws Exception {
    super.givenInvalidAuthentication_whenRootAccessed_thenStatus(SC_OK);
  }

  @Test
  public void givenInvalidAuthentication_whenShortLinkAccessed_thenOk() throws Exception {
    super.givenInvalidAuthentication_whenShortLinkAccessed_thenStatus(SC_OK);
  }

  @Test
  public void givenInvalidAuthentication_whenApiAccessed_thenForbidden() throws Exception {
    super.givenInvalidAuthentication_whenApiAccessed_thenStatus(SC_FORBIDDEN);
  }

  @Override
  protected void withValidAuthentication() {
    withAuthentication(AUTHORIZED_AUTHENTICATION_TOKEN);
  }

  @Override
  protected void withInvalidAuthentication() {
    withAuthentication(UNAUTHORIZED_AUTHENTICATION_TOKEN);
  }

  private void withAuthentication(Authentication authentication) {
    SecurityContext context = createEmptyContext();
    context.setAuthentication(authentication);
    setContext(context);
  }

}
