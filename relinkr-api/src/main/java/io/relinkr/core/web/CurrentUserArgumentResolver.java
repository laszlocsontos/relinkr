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
