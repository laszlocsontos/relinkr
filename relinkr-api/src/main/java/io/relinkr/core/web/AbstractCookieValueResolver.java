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

public abstract class AbstractCookieValueResolver<V> implements CookieValueResolver<V> {

  @Override
  public Optional<V> resolveValue(HttpServletRequest request) {
    return getCookieManager().getCookie(request).flatMap(this::fromString);
  }

  @Override
  public void setValue(HttpServletResponse response, V value) {
    Optional<String> cookieValue = Optional.ofNullable(value).flatMap(this::toString);
    if (cookieValue.isPresent()) {
      getCookieManager().addCookie(response, cookieValue.get());
      return;
    }

    getCookieManager().removeCookie(response);
  }

  protected abstract Optional<V> fromString(String value);

  protected abstract Optional<String> toString(V value);

  protected abstract CookieManager getCookieManager();

}
