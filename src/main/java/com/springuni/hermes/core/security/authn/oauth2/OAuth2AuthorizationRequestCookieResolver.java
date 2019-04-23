package com.springuni.hermes.core.security.authn.oauth2;

import com.springuni.hermes.core.web.CookieValueResolver;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public interface OAuth2AuthorizationRequestCookieResolver
        extends CookieValueResolver<OAuth2AuthorizationRequest> {

    default Optional<OAuth2AuthorizationRequest> resolveRequest(HttpServletRequest request) {
        return resolveValue(request);
    }

    default void setRequest(HttpServletResponse response, OAuth2AuthorizationRequest value) {
        setValue(response, value);
    }

}
