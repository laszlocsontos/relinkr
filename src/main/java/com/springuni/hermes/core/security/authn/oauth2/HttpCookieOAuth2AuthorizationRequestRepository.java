package com.springuni.hermes.core.security.authn.oauth2;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.STATE;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.Assert;

/**
 * An implementation of an {@link AuthorizationRequestRepository} that stores {@link
 * OAuth2AuthorizationRequest} in the {@code javax.servlet.http.Cookie}.
 */
public final class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final OAuth2AuthorizationRequestsCookieResolver authorizationRequestCookieResolver;

    public HttpCookieOAuth2AuthorizationRequestRepository(
            OAuth2AuthorizationRequestsCookieResolver authorizationRequestCookieResolver) {

        this.authorizationRequestCookieResolver = authorizationRequestCookieResolver;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(
            @NonNull HttpServletRequest request) {

        String stateParameter = this.getStateParameter(request);
        if (stateParameter == null) {
            return null;
        }

        Map<String, OAuth2AuthorizationRequest> authorizationRequests =
                getAuthorizationRequests(request);

        return authorizationRequests.get(stateParameter);
    }

    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest,
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {

        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }

        String state = authorizationRequest.getState();
        Assert.hasText(state, "authorizationRequest.state cannot be empty");

        Map<String, OAuth2AuthorizationRequest> authorizationRequests =
                getAuthorizationRequests(request);

        authorizationRequests.put(state, authorizationRequest);
        authorizationRequestCookieResolver.setValue(response, authorizationRequests);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {

        String stateParameter = this.getStateParameter(request);
        if (stateParameter == null) {
            return null;
        }

        Map<String, OAuth2AuthorizationRequest> authorizationRequests =
                getAuthorizationRequests(request);

        OAuth2AuthorizationRequest originalRequest = authorizationRequests.remove(stateParameter);

        if (!authorizationRequests.isEmpty()) {
            authorizationRequestCookieResolver.setValue(response, authorizationRequests);
        } else {
            authorizationRequestCookieResolver.setValue(response, null);
        }

        return originalRequest;
    }

    private String getStateParameter(HttpServletRequest request) {
        return request.getParameter(STATE);
    }

    private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(
            HttpServletRequest request) {

        return authorizationRequestCookieResolver.resolveRequests(request).orElse(new HashMap<>());
    }

}
