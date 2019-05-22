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

import io.relinkr.core.security.authn.annotation.CurrentUser;
import io.relinkr.user.model.UserId;
import java.security.Principal;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return (parameter.getParameterAnnotation(CurrentUser.class) != null)
        && UserId.class.equals(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

    Principal principal = SecurityContextHolder.getContext().getAuthentication();
    if (principal == null) {
      return null;
    }

    String principalName = principal.getName();

    try {
      long userId = NumberUtils.parseNumber(principalName, Long.class);
      return UserId.of(userId);
    } catch (IllegalArgumentException iea) {
      throw new IllegalArgumentException(
          principalName + " is not convertable to " + parameter.getParameterType(), iea
      );
    }
  }

}
