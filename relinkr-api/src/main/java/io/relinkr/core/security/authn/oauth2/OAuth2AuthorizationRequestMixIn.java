package io.relinkr.core.security.authn.oauth2;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;

@JsonDeserialize(using = OAuth2AuthorizationRequestDeserializer.class)
@JsonAutoDetect(
    fieldVisibility = NONE,
    getterVisibility = PUBLIC_ONLY,
    isGetterVisibility = PUBLIC_ONLY)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class OAuth2AuthorizationRequestMixIn {

  @JsonUnwrapped(prefix = "grantType.")
  AuthorizationGrantType getGrantType() {
    return null;
  }

  @JsonIgnore
  OAuth2AuthorizationResponseType getResponseType() {
    return null;
  }

}
