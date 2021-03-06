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

import static io.relinkr.core.security.authn.oauth2.OAuth2AuthorizationRequestCookieResolverImpl.COOKIE_MAX_AGE;
import static io.relinkr.core.security.authn.oauth2.OAuth2AuthorizationRequestCookieResolverImpl.COOKIE_NAME;
import static io.relinkr.core.security.authn.oauth2.OAuth2AuthorizationRequestCookieResolverImpl.OAUTH2_REQUEST_COOKIE_SECRET_KEY_PROPERTY;
import static io.relinkr.test.Mocks.JWS_OAUTH2_AUTHORIZATION_REQUEST_COOKIE_VALUE;
import static io.relinkr.test.Mocks.OAUTH2_AUTHORIZATION_REQUEST;
import static io.relinkr.test.Mocks.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_SECRET_KEY;
import static io.relinkr.test.Mocks.OAUTH2_STATE;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import io.relinkr.core.web.AbstractCookieValueResolver;
import io.relinkr.core.web.AbstractCookieValueResolverTest;
import java.util.Map;
import org.hamcrest.Matchers;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class OAuth2AuthorizationRequestTest
    extends AbstractCookieValueResolverTest<Map<String, OAuth2AuthorizationRequest>> {

  public OAuth2AuthorizationRequestTest() {
    super(
        COOKIE_NAME,
        COOKIE_MAX_AGE,
        singletonMap(OAUTH2_STATE, OAUTH2_AUTHORIZATION_REQUEST),
        JWS_OAUTH2_AUTHORIZATION_REQUEST_COOKIE_VALUE
    );
  }

  @Override
  protected void setUpEnvironment(MockEnvironment environment) {
    environment.setProperty(
        OAUTH2_REQUEST_COOKIE_SECRET_KEY_PROPERTY,
        OAUTH2_AUTHORIZATION_REQUEST_COOKIE_SECRET_KEY
    );
  }

  @Override
  protected AbstractCookieValueResolver<Map<String, OAuth2AuthorizationRequest>> createCookieValueResolver(
      Environment environment) {

    return new OAuth2AuthorizationRequestCookieResolverImpl(environment);
  }

  @Override
  protected void assertCookieValue(
      Map<String, OAuth2AuthorizationRequest> expectedRequests,
      Map<String, OAuth2AuthorizationRequest> actualRequests) {

    // Sanity check
    assertThat(expectedRequests, Matchers.hasKey(OAUTH2_STATE));
    assertThat(actualRequests, Matchers.hasKey(OAUTH2_STATE));

    OAuth2AuthorizationRequest expected = expectedRequests.get(OAUTH2_STATE);
    OAuth2AuthorizationRequest actual = actualRequests.get(OAUTH2_STATE);

    assertEquals(expected.getGrantType(), actual.getGrantType());
    assertEquals(expected.getResponseType(), actual.getResponseType());
    assertEquals(expected.getClientId(), actual.getClientId());
    assertEquals(expected.getScopes(), actual.getScopes());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getAuthorizationUri(), actual.getAuthorizationUri());
    assertEquals(expected.getRedirectUri(), actual.getRedirectUri());
    assertEquals(expected.getAdditionalParameters(), actual.getAdditionalParameters());
  }

}
