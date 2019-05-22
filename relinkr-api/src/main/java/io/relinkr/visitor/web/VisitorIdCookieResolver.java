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
 
package io.relinkr.visitor.web;

import io.relinkr.core.web.CookieValueResolver;
import io.relinkr.visitor.model.VisitorId;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VisitorIdCookieResolver extends CookieValueResolver<VisitorId> {

  default Optional<VisitorId> resolveVisitorId(HttpServletRequest request) {
    return resolveValue(request);
  }

  default void setVisitorId(HttpServletResponse response, VisitorId visitorId) {
    setValue(response, visitorId);
  }

}
