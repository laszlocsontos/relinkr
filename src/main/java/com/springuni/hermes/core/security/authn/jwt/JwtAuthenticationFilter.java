package com.springuni.hermes.core.security.authn.jwt;

import com.springuni.hermes.core.web.AjaxRequestMatcher;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

/**
 * Created by lcsontos on 5/18/17.
 */
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER_TOKEN_PREFIX = "Bearer";

    private static final RequestMatcher AJAX_REQUEST_MATCHER = new AjaxRequestMatcher();

    private static final RequestMatcher AUTHORIZATION_HEADER_MATCHER =
            new RequestHeaderRequestMatcher(AUTHORIZATION_HEADER);

    private static final AuthenticationSuccessHandler NOOP_AUTH_SUCCESS_HANDLER =
            (request, response, authentication) -> {
            };

    private final JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver;

    public JwtAuthenticationFilter(
            RequestMatcher requiresAuthenticationRequestMatcher,
            AuthenticationManager authenticationManager,
            AuthenticationFailureHandler authenticationFailureHandler,
            JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver) {

        super(requiresAuthenticationRequestMatcher);

        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(NOOP_AUTH_SUCCESS_HANDLER);
        setAuthenticationFailureHandler(authenticationFailureHandler);

        this.authenticationTokenCookieResolver = authenticationTokenCookieResolver;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        Optional<String> bearerToken = Optional.empty();

        if (AJAX_REQUEST_MATCHER.matches(request)) {
            bearerToken = authenticationTokenCookieResolver.resolveToken(request);
        } else if (AUTHORIZATION_HEADER_MATCHER.matches(request)) {
            bearerToken = extractFromAuthorizationHeader(request);
        }

        return bearerToken.map(JwtAuthenticationToken::of)
                .map(getAuthenticationManager()::authenticate)
                .orElseThrow(() -> new BadCredentialsException("Null or empty token"));
    }

    private Optional<String> extractFromAuthorizationHeader(HttpServletRequest request) {
        String authHeaderValue = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isEmpty(authHeaderValue)) {
            log.debug("Authorization header is empty.");
            return Optional.empty();
        }

        if (!StringUtils.substringMatch(authHeaderValue, 0, BEARER_TOKEN_PREFIX)) {
            log.debug(
                    "Token prefix {} in Authorization header was not found.",
                    BEARER_TOKEN_PREFIX
            );

            return Optional.empty();
        }

        String bearerToken = authHeaderValue.substring(BEARER_TOKEN_PREFIX.length() + 1);
        if (!StringUtils.hasText(bearerToken)) {
            return Optional.empty();
        }

        return Optional.of(bearerToken);
    }

}
