package com.springuni.hermes.core.security.authn.handler;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.security.authn.jwt.JwtAuthenticationService;
import com.springuni.hermes.core.web.RestErrorResponse;
import com.springuni.hermes.test.web.BaseServletTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAuthenticationSuccessHandlerTest extends BaseServletTest {

    private ObjectMapper objectMapper;

    @Mock
    private JwtAuthenticationService jwtAuthenticationService;

    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Before
    public void setUp() {
        super.setUp();

        objectMapper = new ObjectMapper();

        authenticationSuccessHandler =
                new DefaultAuthenticationSuccessHandler(objectMapper, jwtAuthenticationService);
    }

    @Test
    public void givenJwtCreated_whenOnSuccess_thenTokenSent() throws Exception {
        given(jwtAuthenticationService.createJwtToken(any(Authentication.class), anyInt()))
                .willReturn("token");

        Authentication testAuthentication = new TestingAuthenticationToken("test", null);

        authenticationSuccessHandler.onAuthenticationSuccess(request, response, testAuthentication);

        JsonNode tokenResponse =
                objectMapper.readValue(response.getContentAsByteArray(), JsonNode.class);

        assertEquals("token", tokenResponse.path("token").asText());
    }

    @Test
    public void givenJwtCreationFailed_whenOnSuccess_thenErrorSent() throws Exception {
        final String errorMessage = "Couldn't create JWT";

        given(jwtAuthenticationService.createJwtToken(any(Authentication.class), anyInt()))
                .willThrow(new InternalAuthenticationServiceException(errorMessage));

        Authentication testAuthentication = new TestingAuthenticationToken("test", null);

        authenticationSuccessHandler.onAuthenticationSuccess(request, response, testAuthentication);

        RestErrorResponse tokenResponse =
                objectMapper.readValue(response.getContentAsByteArray(), RestErrorResponse.class);

        assertEquals(SC_INTERNAL_SERVER_ERROR, tokenResponse.getStatusCode());
        assertEquals(errorMessage, tokenResponse.getDetailMessage());
    }

}
