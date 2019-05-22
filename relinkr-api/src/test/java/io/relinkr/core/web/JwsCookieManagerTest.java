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

import static io.relinkr.test.Mocks.JWS_VISITOR_COOKIE_SECRET_KEY;

import java.time.Duration;

public class JwsCookieManagerTest extends AbstractCookieManagerTest {

  @Override
  CookieManager createCookieManager(String cookieName, Duration cookieMaxAgeDuration) {
    return new JwsCookieManager(
        cookieName, cookieMaxAgeDuration, true, JWS_VISITOR_COOKIE_SECRET_KEY
    );
  }

}
