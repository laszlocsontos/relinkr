package com.springuni.hermes.core.security.authn.signin;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.BaseServletTest;
import com.springuni.hermes.core.web.RestErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RunWith(MockitoJUnitRunner.class)
public class SignInFailureHandlerTest extends BaseServletTest {

    private ObjectMapper objectMapper;
    private AuthenticationFailureHandler handler;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        objectMapper = new ObjectMapper();
        handler = new SignInFailureHandler(objectMapper);
    }

    @Test
    public void onAuthenticationFailure() throws Exception {
        handler.onAuthenticationFailure(request, response, new BadCredentialsException("bad"));

        RestErrorResponse errorResponse =
                objectMapper.readValue(response.getContentAsByteArray(), RestErrorResponse.class);

        assertEquals(UNAUTHORIZED.value(), errorResponse.getStatusCode());
        assertEquals(APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals("bad", errorResponse.getDetailMessage());
    }

}
