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

package io.relinkr.core.web;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Strategy interface for resolving cookie values from an {@link HttpServletRequest}.
 *
 * @param <V> Cookie value's type
 */
public interface CookieValueResolver<V> {

  /**
   * Resolve value from a cookie.
   *
   * @param request an {@link HttpServletRequest} to resolve the value from
   * @return the cookie's value if exists, empty otherwise
   */
  Optional<V> resolveValue(HttpServletRequest request);

  /**
   * Set the cookie's value and send it along with the given {@link HttpServletResponse}.
   *
   * @param response an {@link HttpServletRequest} to send the cookie's value with
   * @param value cookie value to set
   */
  void setValue(HttpServletResponse response, V value);

}
