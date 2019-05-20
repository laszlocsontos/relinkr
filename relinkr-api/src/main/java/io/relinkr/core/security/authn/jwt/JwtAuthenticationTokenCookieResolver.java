package io.relinkr.core.security.authn.jwt;

import io.relinkr.core.web.CookieValueResolver;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtAuthenticationTokenCookieResolver extends CookieValueResolver<String> {

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
