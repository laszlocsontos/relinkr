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

import static org.springframework.security.oauth2.core.AuthorizationGrantType.IMPLICIT;
import static org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest.authorizationCode;
import static org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest.implicit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * Custom deserializer for {@link OAuth2AuthorizationRequest}.
 */
class OAuth2AuthorizationRequestDeserializer extends JsonDeserializer<OAuth2AuthorizationRequest> {

  private static final TypeReference<Set<String>> VALUE_REF_STRING_SET =
      new TypeReference<Set<String>>() { };

  private static final TypeReference<Map<String, Object>> VALUE_REF_STRING_OBJECT_MAP =
      new TypeReference<Map<String, Object>>() { };

  @Override
  public OAuth2AuthorizationRequest deserialize(JsonParser jp, DeserializationContext dctx)
      throws IOException {

    ObjectMapper mapper = (ObjectMapper) jp.getCodec();
    JsonNode jsonNode = mapper.readTree(jp);

    AuthorizationGrantType authorizationGrantType =
        new AuthorizationGrantType(readJsonNode(jsonNode, "grantType.value").asText());

    OAuth2AuthorizationRequest.Builder builder;
    if (IMPLICIT.equals(authorizationGrantType)) {
      builder = implicit();
    } else {
      builder = authorizationCode();
    }

    Set<String> scopes = mapper.convertValue(
        jsonNode.get("authorities"), VALUE_REF_STRING_SET);

    Map<String, Object> additionalParameters = mapper.convertValue(
        jsonNode.get("additionalParameters"), VALUE_REF_STRING_OBJECT_MAP);

    return builder
        .clientId(readJsonNode(jsonNode, "clientId").asText())
        .authorizationUri(readJsonNode(jsonNode, "authorizationUri").asText())
        .redirectUri(readJsonNode(jsonNode, "redirectUri").asText())
        .scopes(scopes)
        .state(readJsonNode(jsonNode, "state").asText())
        .additionalParameters(additionalParameters)
        .build();
  }

  private JsonNode readJsonNode(JsonNode jsonNode, String field) {
    return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
  }

}
