package io.relinkr.core.security.authn.oauth2;

import static io.relinkr.core.security.authn.oauth2.OAuth2LoginAuthenticationSuccessHandler.HALF_AN_HOUR;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import io.relinkr.core.security.authn.jwt.JwtAuthenticationService;
import io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

@RunWith(MockitoJUnitRunner.class)
public class OAuth2LoginAuthenticationSuccessHandlerTest extends
        AbstractOAuth2LoginAuthenticationHandlerTest<OAuth2LoginAuthenticationSuccessHandler> {

    @Mock
    private JwtAuthenticationService jwtAuthenticationService;

    @Mock
    private JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver;

    @Test(expected = IllegalArgumentException.class)
    public void givenNullAuthentication_whenOnAuthenticationSuccess_thenIllegalArgumentException()
            throws Exception {

        handler.onAuthenticationSuccess(request, response, null);
    }

    @Test
    public void givenAuthenticationException_whenOnAuthenticationSuccess_thenRedirectAndCookieSet()
            throws Exception {

        Authentication authentication = new TestingAuthenticationToken("little", "bunny");

        given(jwtAuthenticationService.createJwtToken(authentication, HALF_AN_HOUR))
                .willReturn("token");

        handler.onAuthenticationSuccess(
                request,
                response,
                authentication
        );

        assertEquals(LOGIN_URL, response.getRedirectedUrl());

        then(authenticationTokenCookieResolver).should().setToken(response, "token");
    }

    @Override
    OAuth2LoginAuthenticationSuccessHandler createHandler() {
        return new OAuth2LoginAuthenticationSuccessHandler(
                jwtAuthenticationService, authenticationTokenCookieResolver
        );
    }

}
