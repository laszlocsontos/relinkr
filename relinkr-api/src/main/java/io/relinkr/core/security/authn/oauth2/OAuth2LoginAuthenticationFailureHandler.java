package io.relinkr.core.security.authn.oauth2;

import io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RequiredArgsConstructor
public class OAuth2LoginAuthenticationFailureHandler
        extends AbstractOAuth2LoginAuthenticationHandler implements AuthenticationFailureHandler {

    private final JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response,
            @NonNull AuthenticationException exception)
            throws IOException {

        authenticationTokenCookieResolver.removeToken(response);

        sendRedirect(
                request, response,
                builder -> builder.queryParam("error", "{error}")
                        .buildAndExpand(exception.getMessage())
                        .encode()
        );
    }

}
