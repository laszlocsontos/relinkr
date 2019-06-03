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

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;

/**
 * Mix-in for augmenting {@link org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest}
 * with annotations fine tuning its serialization method to JSON.
 */
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
