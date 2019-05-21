package io.relinkr.core.security.authn.oauth2;

import static io.relinkr.test.Mocks.OAUTH2_AUTHORIZATION_REQUEST;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Slf4j
public class OAuth2AuthorizationRequestMixInTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Before
  public void setUp() {
    objectMapper.addMixIn(
        OAuth2AuthorizationRequest.class, OAuth2AuthorizationRequestMixIn.class
    );
  }

  @Test
  public void shouldDeserializeCorrectly() throws Exception {
    String json = objectMapper.writeValueAsString(OAUTH2_AUTHORIZATION_REQUEST);
    log.info(json);

    OAuth2AuthorizationRequest deserializedRequest =
        objectMapper.readValue(json, OAuth2AuthorizationRequest.class);

    assertEquals(OAUTH2_AUTHORIZATION_REQUEST.getGrantType(),
        deserializedRequest.getGrantType());
    assertEquals(OAUTH2_AUTHORIZATION_REQUEST.getResponseType(),
        deserializedRequest.getResponseType());
    assertEquals(OAUTH2_AUTHORIZATION_REQUEST.getClientId(), deserializedRequest.getClientId());
    assertEquals(OAUTH2_AUTHORIZATION_REQUEST.getScopes(), deserializedRequest.getScopes());
    assertEquals(OAUTH2_AUTHORIZATION_REQUEST.getState(), deserializedRequest.getState());
    assertEquals(
        OAUTH2_AUTHORIZATION_REQUEST.getAuthorizationUri(),
        deserializedRequest.getAuthorizationUri()
    );
    assertEquals(OAUTH2_AUTHORIZATION_REQUEST.getRedirectUri(),
        deserializedRequest.getRedirectUri());
    assertEquals(
        OAUTH2_AUTHORIZATION_REQUEST.getAdditionalParameters(),
        deserializedRequest.getAdditionalParameters()
    );
  }

}
