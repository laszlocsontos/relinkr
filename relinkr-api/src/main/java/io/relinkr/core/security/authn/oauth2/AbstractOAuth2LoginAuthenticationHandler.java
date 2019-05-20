package io.relinkr.core.security.authn.oauth2;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import java.io.IOException;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

abstract class AbstractOAuth2LoginAuthenticationHandler implements EnvironmentAware {

    static final String FRONTEND_LOGIN_URL_PROPERTY = "relinkr.frontend.login-url";

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private String loginUrl;

    @Override
    public void setEnvironment(Environment environment) {
        loginUrl = environment.getRequiredProperty(FRONTEND_LOGIN_URL_PROPERTY);
    }

    void sendRedirect(
            HttpServletRequest request, HttpServletResponse response,
            Function<UriComponentsBuilder, UriComponents> uriComponentsCreator) throws IOException {

        UriComponentsBuilder uriComponentsBuilder = fromHttpUrl(loginUrl);
        UriComponents uriComponents = uriComponentsCreator.apply(uriComponentsBuilder);

        redirectStrategy.sendRedirect(request, response, uriComponents.toString());
    }

}
