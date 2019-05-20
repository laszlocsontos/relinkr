package io.relinkr.core.security.authn.oauth2;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.then;

import io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;

@RunWith(MockitoJUnitRunner.class)
public class OAuth2LoginAuthenticationFailureHandlerTest
        extends
        AbstractOAuth2LoginAuthenticationHandlerTest<OAuth2LoginAuthenticationFailureHandler> {

    @Mock
    private JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver;

    @Test(expected = IllegalArgumentException.class)
    public void givenNullAuthenticationException_whenOnAuthenticationFailure_thenIllegalArgumentException()
            throws Exception {

        handler.onAuthenticationFailure(request, response, null);
    }

    @Test
    public void givenAuthenticationException_whenOnAuthenticationFailure_thenRedirectAndCookieRemoved()
            throws Exception {

        handler.onAuthenticationFailure(
                request,
                response,
                new BadCredentialsException("bad")
        );

        assertEquals(
                LOGIN_URL + "?error=bad",
                response.getRedirectedUrl()
        );

        then(authenticationTokenCookieResolver).should().removeToken(response);
    }

    @Override
    OAuth2LoginAuthenticationFailureHandler createHandler() {
        return new OAuth2LoginAuthenticationFailureHandler(authenticationTokenCookieResolver);
    }

}
