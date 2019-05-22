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

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.STATE;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * An implementation of an {@link AuthorizationRequestRepository} that stores {@link
 * OAuth2AuthorizationRequest} in the {@code javax.servlet.http.Cookie}.
 */
@RequiredArgsConstructor
public final class HttpCookieOAuth2AuthorizationRequestRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  private final OAuth2AuthorizationRequestsCookieResolver authorizationRequestCookieResolver;

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(
      @NonNull HttpServletRequest request) {

    Optional<String> stateParameter = getStateParameter(request);
    if (!stateParameter.isPresent()) {
      return null;
    }

    Map<String, OAuth2AuthorizationRequest> authorizationRequests =
        getAuthorizationRequests(request);

    return authorizationRequests.get(stateParameter.get());
  }

  @Override
  public void saveAuthorizationRequest(
      OAuth2AuthorizationRequest authorizationRequest,
      @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {

    if (authorizationRequest == null) {
      removeAuthorizationRequest(request, response);
      return;
    }

    String state = authorizationRequest.getState();
    Assert.hasText(state, "authorizationRequest.state cannot be empty");

    Map<String, OAuth2AuthorizationRequest> authorizationRequests =
        getAuthorizationRequests(request);

    authorizationRequests.put(state, authorizationRequest);
    authorizationRequestCookieResolver.setRequests(response, authorizationRequests);
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(
      @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {

    Optional<String> stateParameter = this.getStateParameter(request);
    if (!stateParameter.isPresent()) {
      return null;
    }

    Map<String, OAuth2AuthorizationRequest> authorizationRequests =
        getAuthorizationRequests(request);

    OAuth2AuthorizationRequest originalRequest =
        authorizationRequests.remove(stateParameter.get());

    if (!authorizationRequests.isEmpty()) {
      authorizationRequestCookieResolver.setRequests(response, authorizationRequests);
    } else {
      authorizationRequestCookieResolver.setRequests(response, null);
    }

    return originalRequest;
  }

  private Optional<String> getStateParameter(HttpServletRequest request) {
    return Optional.ofNullable(request.getParameter(STATE)).filter(StringUtils::hasText);
  }

  private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(
      HttpServletRequest request) {

    return authorizationRequestCookieResolver.resolveRequests(request)
        .map(HashMap::new).orElse(new HashMap<>());
  }

}
