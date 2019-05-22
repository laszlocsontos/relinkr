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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.relinkr.core.web.AbstractCookieValueResolver;
import io.relinkr.core.web.CookieManager;
import io.relinkr.core.web.JwsCookieManager;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class OAuth2AuthorizationRequestCookieResolverImpl
    extends AbstractCookieValueResolver<Map<String, OAuth2AuthorizationRequest>>
    implements OAuth2AuthorizationRequestsCookieResolver {

  static final String COOKIE_NAME = "oar";

  static final Duration COOKIE_MAX_AGE = Duration.ofMinutes(1);

  static final String OAUTH2_REQUEST_COOKIE_SECRET_KEY_PROPERTY =
      "relinkr.cookie.oauth2-request-secret-key";

  private static final TypeReference<Map<String, OAuth2AuthorizationRequest>> TYPE_REFERENCE =
      new TypeReference<Map<String, OAuth2AuthorizationRequest>>() {
      };

  private final CookieManager cookieManager;
  private final ObjectMapper objectMapper;

  @Autowired
  public OAuth2AuthorizationRequestCookieResolverImpl(Environment environment) {
    String secretKey =
        environment.getRequiredProperty(OAUTH2_REQUEST_COOKIE_SECRET_KEY_PROPERTY);

    cookieManager = new JwsCookieManager(COOKIE_NAME, COOKIE_MAX_AGE, secretKey);

    // We want to leave the ObjectMapper in the app-context unchanged
    objectMapper = new ObjectMapper().addMixIn(
        OAuth2AuthorizationRequest.class, OAuth2AuthorizationRequestMixIn.class
    );
  }

  @Override
  protected CookieManager getCookieManager() {
    return cookieManager;
  }

  @Override
  protected Optional<Map<String, OAuth2AuthorizationRequest>> fromString(String value) {
    if (!StringUtils.hasText(value)) {
      return Optional.empty();
    }

    try {
      return Optional.of(objectMapper.readValue(value, TYPE_REFERENCE));
    } catch (IOException ioe) {
      log.debug(ioe.getMessage(), ioe);
      return Optional.empty();
    }
  }

  @Override
  protected Optional<String> toString(Map<String, OAuth2AuthorizationRequest> value) {
    if (CollectionUtils.isEmpty(value)) {
      return Optional.empty();
    }

    try {
      return Optional.of(objectMapper.writeValueAsString(value));
    } catch (IOException ioe) {
      log.debug(ioe.getMessage(), ioe);
      return Optional.empty();
    }
  }

}
