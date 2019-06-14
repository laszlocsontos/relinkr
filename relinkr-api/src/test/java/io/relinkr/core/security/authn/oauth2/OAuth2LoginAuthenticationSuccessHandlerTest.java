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

import static io.relinkr.core.security.authn.oauth2.OAuth2LoginAuthenticationSuccessHandler.HALF_AN_HOUR;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import io.relinkr.core.security.authn.jwt.JwtAuthenticationService;
import io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

@RunWith(MockitoJUnitRunner.class)
public class OAuth2LoginAuthenticationSuccessHandlerTest extends
    AbstractOAuth2LoginAuthenticationHandlerTest<OAuth2LoginAuthenticationSuccessHandler> {

  @Mock
  private JwtAuthenticationService jwtAuthenticationService;

  @Mock
  private JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver;

  @Test(expected = IllegalArgumentException.class)
  public void givenNullAuthentication_whenOnAuthenticationSuccess_thenIllegalArgumentException()
      throws Exception {

    handler.onAuthenticationSuccess(request, response, null);
  }

  @Test
  public void givenNoAuthenticationException_whenOnAuthenticationSuccess_thenRedirectAndCookieSet()
      throws Exception {

    Authentication authentication = new TestingAuthenticationToken("little", "bunny");

    given(jwtAuthenticationService.createJwtToken(authentication, HALF_AN_HOUR))
        .willReturn("token");

    handler.onAuthenticationSuccess(
        request,
        response,
        authentication
    );

    assertEquals(LOGIN_URL, response.getRedirectedUrl());

    then(authenticationTokenCookieResolver).should().setToken(response, "token");
  }

  @Test
  public void givenAuthenticationException_whenOnAuthenticationSuccess_thenRedirectAndNoCookieSet()
      throws Exception {

    Authentication authentication = new TestingAuthenticationToken("little", "bunny");

    given(jwtAuthenticationService.createJwtToken(authentication, HALF_AN_HOUR))
        .willThrow(new InternalAuthenticationServiceException("error"));

    handler.onAuthenticationSuccess(
        request,
        response,
        authentication
    );

    assertEquals(LOGIN_URL_WITH_ERROR, response.getRedirectedUrl());

    then(authenticationTokenCookieResolver).should(never()).setToken(response, "token");
  }

  @Override
  OAuth2LoginAuthenticationSuccessHandler createHandler() {
    return new OAuth2LoginAuthenticationSuccessHandler(
        jwtAuthenticationService, authenticationTokenCookieResolver
    );
  }

}
