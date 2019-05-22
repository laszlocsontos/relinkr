/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

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
