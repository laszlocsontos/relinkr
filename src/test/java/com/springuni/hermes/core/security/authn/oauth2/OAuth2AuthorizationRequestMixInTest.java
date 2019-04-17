package com.springuni.hermes.core.security.authn.oauth2;

import static com.springuni.hermes.core.security.authn.WebSecurityConfig.OAUTH2_LOGIN_PROCESSES_BASE_URI;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.config.oauth2.client.CommonOAuth2Provider.GOOGLE;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REGISTRATION_ID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Slf4j
public class OAuth2AuthorizationRequestMixInTest {

    private static final String CLIENT_ID = "1234";
    private static final String CLIENT_SECRET = "1234";
    private static final String CLIENT_REG_ID = "google";
    private static final String STATE = "state";

    private static final String BASE_URI = "http://localhost";

    private static final String REDIRECT_URI =
            BASE_URI + OAUTH2_LOGIN_PROCESSES_BASE_URI + "/" + CLIENT_REG_ID;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        objectMapper.addMixIn(
                OAuth2AuthorizationRequest.class, OAuth2AuthorizationRequestMixIn.class
        );
    }

    @Test
    public void shouldDeserializeCorrectly() throws Exception {
        ClientRegistration clientRegistration = GOOGLE.getBuilder(CLIENT_REG_ID)
                .authorizationGrantType(AUTHORIZATION_CODE)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .scope("email")
                .build();

        OAuth2AuthorizationRequest originalRequest = OAuth2AuthorizationRequest
                .authorizationCode()
                .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .clientId(CLIENT_ID)
                .state(STATE)
                .redirectUri(REDIRECT_URI)
                .additionalParameters(singletonMap(REGISTRATION_ID, CLIENT_REG_ID))
                .build();

        String json = objectMapper.writeValueAsString(originalRequest);
        log.info(json);

        OAuth2AuthorizationRequest deserializedRequest =
                objectMapper.readValue(json, OAuth2AuthorizationRequest.class);

        assertEquals(originalRequest.getGrantType(), deserializedRequest.getGrantType());
        assertEquals(originalRequest.getResponseType(), deserializedRequest.getResponseType());
        assertEquals(originalRequest.getClientId(), deserializedRequest.getClientId());
        assertEquals(originalRequest.getScopes(), deserializedRequest.getScopes());
        assertEquals(originalRequest.getState(), deserializedRequest.getState());
        assertEquals(
                originalRequest.getAuthorizationUri(), deserializedRequest.getAuthorizationUri()
        );
        assertEquals(originalRequest.getRedirectUri(), deserializedRequest.getRedirectUri());
        assertEquals(
                originalRequest.getAdditionalParameters(),
                deserializedRequest.getAdditionalParameters()
        );
    }

}
