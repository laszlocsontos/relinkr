package com.springuni.hermes.core.security.authn.oauth2;

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

class OAuth2AuthorizationRequestDeserializer extends JsonDeserializer<OAuth2AuthorizationRequest> {

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
                jsonNode.get("authorities"), new TypeReference<Set<String>>() {});

        Map<String, Object> additionalParameters = mapper.convertValue(
                jsonNode.get("additionalParameters"), new TypeReference<Map<String, Object>>() {});

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
