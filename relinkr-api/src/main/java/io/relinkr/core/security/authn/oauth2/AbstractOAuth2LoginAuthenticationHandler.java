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

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import java.io.IOException;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

abstract class AbstractOAuth2LoginAuthenticationHandler implements EnvironmentAware {

  static final String FRONTEND_LOGIN_URL_PROPERTY = "relinkr.frontend.login-url";

  private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  private String loginUrl;

  @Override
  public void setEnvironment(Environment environment) {
    loginUrl = environment.getRequiredProperty(FRONTEND_LOGIN_URL_PROPERTY);
  }

  void sendRedirect(
      HttpServletRequest request, HttpServletResponse response,
      Function<UriComponentsBuilder, UriComponents> uriComponentsCreator) throws IOException {

    UriComponentsBuilder uriComponentsBuilder = fromHttpUrl(loginUrl);
    UriComponents uriComponents = uriComponentsCreator.apply(uriComponentsBuilder);

    redirectStrategy.sendRedirect(request, response, uriComponents.toString());
  }

}
