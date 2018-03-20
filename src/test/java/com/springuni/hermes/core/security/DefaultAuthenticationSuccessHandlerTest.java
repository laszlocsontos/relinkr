package com.springuni.hermes.core.security;

import static com.springuni.hermes.core.security.DefaultAuthenticationSuccessHandler.ONE_DAY_MINUTES;
import static com.springuni.hermes.core.security.DefaultAuthenticationSuccessHandler.X_SET_AUTHORIZATION_BEARER_HEADER;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import com.springuni.hermes.core.BaseServletTest;
import com.springuni.hermes.core.security.jwt.JwtTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAuthenticationSuccessHandlerTest extends BaseServletTest {

    @Mock
    private Authentication authentication;

    @Mock
    private JwtTokenService jwtTokenService;

    private AuthenticationSuccessHandler handler;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        handler = new DefaultAuthenticationSuccessHandler(jwtTokenService);
    }

    @Test
    public void onAuthenticationSuccess() throws Exception {
        given(jwtTokenService.createJwtToken(authentication, ONE_DAY_MINUTES)).willReturn("token");
        handler.onAuthenticationSuccess(request, response, authentication);
        assertEquals("token", response.getHeader(X_SET_AUTHORIZATION_BEARER_HEADER));
    }

}
