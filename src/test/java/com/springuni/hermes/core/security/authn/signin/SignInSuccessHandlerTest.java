package com.springuni.hermes.core.security.authn.signin;

import com.springuni.hermes.test.web.BaseServletTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RunWith(MockitoJUnitRunner.class)
public class SignInSuccessHandlerTest extends BaseServletTest {

    @Mock
    private Authentication authentication;


    private AuthenticationSuccessHandler handler;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

    }

    @Test
    public void onAuthenticationSuccess() throws Exception {
    }

}
