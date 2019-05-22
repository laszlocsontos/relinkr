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

import io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RequiredArgsConstructor
public class OAuth2LoginAuthenticationFailureHandler
    extends AbstractOAuth2LoginAuthenticationHandler implements AuthenticationFailureHandler {

  private final JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response,
      @NonNull AuthenticationException exception)
      throws IOException {

    authenticationTokenCookieResolver.removeToken(response);

    sendRedirect(
        request, response,
        builder -> builder.queryParam("error", "{error}")
            .buildAndExpand(exception.getMessage())
            .encode()
    );
  }

}
