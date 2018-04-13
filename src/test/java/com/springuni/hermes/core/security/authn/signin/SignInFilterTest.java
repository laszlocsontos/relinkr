package com.springuni.hermes.core.security.authn.signin;

import static com.springuni.hermes.core.security.authn.signin.SignInFilter.SIGNIN_HTTP_METHOD;
import static com.springuni.hermes.core.security.authn.signin.SignInFilter.SIGNIN_PROCESSES_URL;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.BaseFilterTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RunWith(MockitoJUnitRunner.class)
public class SignInFilterTest extends BaseFilterTest {

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Mock
    private AuthenticationFailureHandler authenticationFailureHandler;

    private AbstractAuthenticationProcessingFilter loginFilter;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        ObjectMapper objectMapper = new ObjectMapper();

        loginFilter = new SignInFilter(objectMapper);

        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        loginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        request.setContent(objectMapper.writeValueAsBytes(new SignInRequest("test", "test")));
        request.setMethod(SIGNIN_HTTP_METHOD);
        request.setPathInfo(SIGNIN_PROCESSES_URL);
    }

    @Test
    public void attemptAuthentication_withBadCredentials() throws Exception {
        AuthenticationException authenticationException = new BadCredentialsException("bad");

        given(authenticationManager.authenticate(any(Authentication.class)))
                .willThrow(authenticationException);

        loginFilter.doFilter(request, response, filterChain);

        then(authenticationSuccessHandler).shouldHaveZeroInteractions();

        then(authenticationFailureHandler)
                .should().onAuthenticationFailure(request, response, authenticationException);
    }

    @Test
    public void attemptAuthentication_withBadMethod() throws Exception {
        request.setMethod("GET");

        loginFilter.doFilter(request, response, filterChain);

        then(authenticationSuccessHandler).shouldHaveZeroInteractions();
        then(authenticationFailureHandler).shouldHaveZeroInteractions();
    }

    @Test
    public void attemptAuthentication_withBadRequest() throws Exception {
        request.setContent("bad".getBytes());

        loginFilter.doFilter(request, response, filterChain);

        then(authenticationSuccessHandler).shouldHaveZeroInteractions();

        then(authenticationFailureHandler)
                .should()
                .onAuthenticationFailure(
                        eq(request),
                        eq(response),
                        any(AuthenticationException.class)
                );
    }

    @Test
    public void attemptAuthentication_withGoodCredentials() throws Exception {
        given(authenticationManager.authenticate(any(Authentication.class)))
                .willReturn(authentication);

        loginFilter.doFilter(request, response, filterChain);

        then(authenticationFailureHandler).shouldHaveZeroInteractions();

        then(authenticationSuccessHandler)
                .should().onAuthenticationSuccess(request, response, authentication);
    }

}
