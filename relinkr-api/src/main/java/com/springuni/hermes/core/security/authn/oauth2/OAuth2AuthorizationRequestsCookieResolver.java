package com.springuni.hermes.core.security.authn.oauth2;

import com.springuni.hermes.core.web.CookieValueResolver;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public interface OAuth2AuthorizationRequestsCookieResolver
        extends CookieValueResolver<Map<String, OAuth2AuthorizationRequest>> {

    default Optional<Map<String, OAuth2AuthorizationRequest>> resolveRequests(
            HttpServletRequest request) {

        return resolveValue(request);
    }

    default void setRequests(
            HttpServletResponse response, Map<String, OAuth2AuthorizationRequest> value) {

        setValue(response, value);
    }

}
