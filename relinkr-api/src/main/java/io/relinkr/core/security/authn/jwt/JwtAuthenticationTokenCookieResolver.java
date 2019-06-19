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

import io.relinkr.core.web.CookieValueResolver;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Resolution strategy for extracting JWT tokens from HTTP cookies based on
 * {@link CookieValueResolver}.
 */
public interface JwtAuthenticationTokenCookieResolver extends CookieValueResolver<String> {

  String TOKEN_PAYLOAD_COOKIE_NAME = "atp";
  String TOKEN_SIGNATURE_COOKIE_NAME = "ats";

  default Optional<String> resolveToken(HttpServletRequest request) {
    return resolveValue(request);
  }

  default void setToken(HttpServletResponse response, String token) {
    setValue(response, token);
  }

  default void removeToken(HttpServletResponse response) {
    setToken(response, null);
  }

}
