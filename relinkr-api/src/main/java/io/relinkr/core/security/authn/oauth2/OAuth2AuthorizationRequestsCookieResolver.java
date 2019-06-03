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

import io.relinkr.core.web.CookieValueResolver;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * By default {@link OAuth2AuthorizationRequest} is stored in the session, so that
 * {@link org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter} can pick
 * it up when the OAuth2 provider redirects back after an authentication success or failure. As this
 * application is completely stateless and it doesn't maintain a session, that piece of information
 * is stored in a HTTP cookie.
 */
public interface OAuth2AuthorizationRequestsCookieResolver
    extends CookieValueResolver<Map<String, OAuth2AuthorizationRequest>> {

  default Optional<Map<String, OAuth2AuthorizationRequest>> resolveRequests(
      HttpServletRequest request) {

    return resolveValue(request);
  }

  default void setRequests(
      HttpServletResponse response, Map<String, OAuth2AuthorizationRequest> value) {

    setValue(response, value);
  }

}
