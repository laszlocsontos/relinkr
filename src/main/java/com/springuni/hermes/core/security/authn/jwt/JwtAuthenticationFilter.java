package com.springuni.hermes.core.security.authn.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Created by lcsontos on 5/18/17.
 */
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer";
    public static final String BEARER_TOKEN_ATTRIBUTE = "bearer_token";

    private static final RequestMatcher AUTHORIZATION_BEARER_REQUEST_HEADER_MATCHER =
            new AuthorizationBearerRequestHeaderMatcher();

    private static final AuthenticationSuccessHandler NOOP_AUTH_SUCCESS_HANDLER =
            (request, response, authentication) -> {};

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            AuthenticationFailureHandler authenticationFailureHandler) {

        super(AUTHORIZATION_BEARER_REQUEST_HEADER_MATCHER);

        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(NOOP_AUTH_SUCCESS_HANDLER);
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String bearerToken = (String) request.getAttribute(BEARER_TOKEN_ATTRIBUTE);
        Assert.hasText(bearerToken, "bearerToken should have contained some text.");

        Authentication authRequest = JwtAuthenticationToken.of(bearerToken);

        Authentication authResult;
        try {
            authResult = getAuthenticationManager().authenticate(authRequest);
        } finally {
            request.removeAttribute(BEARER_TOKEN_ATTRIBUTE);
        }

        return authResult;
    }

    public void setIgnoredRequestMatchers(List<RequestMatcher> ignoredRequests) {
        RequestMatcher requestMatcher = new AndRequestMatcher(
                AUTHORIZATION_BEARER_REQUEST_HEADER_MATCHER,
                new NegatedRequestMatcher(new OrRequestMatcher(ignoredRequests))
        );

        setRequiresAuthenticationRequestMatcher(requestMatcher);
    }

    private static class AuthorizationBearerRequestHeaderMatcher implements RequestMatcher {

        @Override
        public boolean matches(HttpServletRequest request) {
            String authHeaderValue = request.getHeader(AUTHORIZATION_HEADER);
            if (StringUtils.isEmpty(authHeaderValue)) {
                log.debug("Authorization header is empty.");
                return false;
            }

            if (!StringUtils.substringMatch(authHeaderValue, 0, BEARER_TOKEN_PREFIX)) {
                log.debug(
                        "Token prefix {} in Authorization header was not found.",
                        BEARER_TOKEN_PREFIX
                );

                return false;
            }

            String bearerToken = authHeaderValue.substring(BEARER_TOKEN_PREFIX.length() + 1);
            if (!StringUtils.hasText(bearerToken)) {
                return false;
            }

            request.setAttribute(BEARER_TOKEN_ATTRIBUTE, bearerToken);

            return true;
        }

    }

}
