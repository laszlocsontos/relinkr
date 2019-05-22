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

import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AjaxRequestMatcher implements RequestMatcher {

  public static final String X_REQUESTED_WITH_HEADER = "X-Requested-With";
  public static final String X_REQUESTED_WITH_VALUE = "XMLHttpRequest";

  private final RequestMatcher delegate =
      new RequestHeaderRequestMatcher(X_REQUESTED_WITH_HEADER, X_REQUESTED_WITH_VALUE);

  @Override
  public boolean matches(@NonNull HttpServletRequest request) {
    return delegate.matches(request);
  }

}
