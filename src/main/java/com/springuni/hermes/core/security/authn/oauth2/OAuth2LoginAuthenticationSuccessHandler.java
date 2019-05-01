package com.springuni.hermes.core.security.authn.oauth2;

import com.springuni.hermes.core.security.authn.jwt.JwtAuthenticationService;
import com.springuni.hermes.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public class OAuth2LoginAuthenticationSuccessHandler
        extends AbstractOAuth2LoginAuthenticationHandler implements AuthenticationSuccessHandler {

    static final int HALF_AN_HOUR = 30;

    private final JwtAuthenticationService jwtAuthenticationService;
    private final JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response,
            @NonNull Authentication authentication)
            throws IOException {

        String bearerToken = jwtAuthenticationService.createJwtToken(authentication, HALF_AN_HOUR);
        authenticationTokenCookieResolver.setToken(response, bearerToken);

        sendRedirect(request, response, UriComponentsBuilder::build);
    }

}
