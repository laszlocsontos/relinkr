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
 
package io.relinkr.core.security.authn.oauth2;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.then;

import io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;

@RunWith(MockitoJUnitRunner.class)
public class OAuth2LoginAuthenticationFailureHandlerTest
    extends
    AbstractOAuth2LoginAuthenticationHandlerTest<OAuth2LoginAuthenticationFailureHandler> {

  @Mock
  private JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver;

  @Test(expected = IllegalArgumentException.class)
  public void givenNullAuthenticationException_whenOnAuthenticationFailure_thenIllegalArgumentException()
      throws Exception {

    handler.onAuthenticationFailure(request, response, null);
  }

  @Test
  public void givenAuthenticationException_whenOnAuthenticationFailure_thenRedirectAndCookieRemoved()
      throws Exception {

    handler.onAuthenticationFailure(
        request,
        response,
        new BadCredentialsException("bad")
    );

    assertEquals(
        LOGIN_URL + "?error=bad",
        response.getRedirectedUrl()
    );

    then(authenticationTokenCookieResolver).should().removeToken(response);
  }

  @Override
  OAuth2LoginAuthenticationFailureHandler createHandler() {
    return new OAuth2LoginAuthenticationFailureHandler(authenticationTokenCookieResolver);
  }

}
