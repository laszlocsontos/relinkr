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

import java.time.Duration;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

/**
 * Helper class for cookie generation based upon {@link CookieGenerator}. Main additions to the
 * original implementations are the following.
 *
 * <ul>
 *   <li>New method {@link CookieManager#getCookie(HttpServletRequest)} returns
 *   {@link Optional}</li>
 *   <li>New method {@link CookieManager#setCookieMaxAge(Duration)} accepts {@link Duration}</li>
 *   <li>Method {@link CookieManager#setCookieSecure(boolean)} enforced cookies being secure</li>
 * </ul>
 *
 */
public class CookieManager extends CookieGenerator {

  public static final int MAX_AGE_AUTO_EXPIRE = -1;

  /**
   * Creates a new {@code CookieManager} with the following properties
   *
   * @param cookieName Cookie name
   * @param cookieMaxAgeDuration Cookies max age, if null expires immediately
   * @param httpOnly Whether or not this is a
   * <a href="https://www.owasp.org/index.php/HttpOnly">HTTPOnly</a> cookie
   */
  public CookieManager(String cookieName, Duration cookieMaxAgeDuration, boolean httpOnly) {
    setCookieName(cookieName);
    setCookieMaxAge(cookieMaxAgeDuration);
    setCookieHttpOnly(httpOnly);
    setCookieSecure(true);
  }

  /**
   * Returns the cookie's value from the given {@link HttpServletRequest}.
   *
   * @param request An {@code HttpServletRequest}
   * @return An {@link Optional} value, empty if the cookie is missing
   */
  public Optional<String> getCookie(HttpServletRequest request) {
    return Optional.ofNullable(getCookieName())
        .map(it -> WebUtils.getCookie(request, it))
        .map(Cookie::getValue)
        .filter(StringUtils::hasText);
  }

  /**
   * Sets the cookies max age. Can be null, in which case the cookie will expire.
   *
   * @param cookieMaxAgeDuration Cookie max age as a {@link Duration}
   */
  public void setCookieMaxAge(Duration cookieMaxAgeDuration) {
    int cookieMaxAge = Optional.ofNullable(cookieMaxAgeDuration)
        .map(Duration::getSeconds)
        .map(Long::intValue)
        .orElse(MAX_AGE_AUTO_EXPIRE);

    setCookieMaxAge(cookieMaxAge);
  }

  @Override
  public final void setCookieSecure(boolean cookieSecure) {
    Assert.isTrue(cookieSecure, "Cookie cannot be insecure");
    super.setCookieSecure(cookieSecure);
  }

}
