package com.springuni.hermes.core.web;

import com.springuni.hermes.core.security.authn.annotation.CurrentUser;
import com.springuni.hermes.user.model.UserId;
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
        return parameter.getParameterAnnotation(CurrentUser.class) != null &&
                UserId.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null) {
            return null;
        }

        String principalName = principal.getName();

        try {
            long userId = NumberUtils.parseNumber(principalName, Long.class);
            return UserId.of(userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    principalName + " is not convertable to " + parameter.getParameterType(), e
            );
        }
    }

}
