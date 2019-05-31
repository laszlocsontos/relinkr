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

package io.relinkr.core.security.authn.jwt;

import io.relinkr.core.web.AjaxRequestMatcher;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

/**
 * Processes authentication when a JWT token is supplied as part of an HTTP request.
 * Leverages {@link AbstractAuthenticationProcessingFilter} for dealing with the details of handling
 * successful and failed authentication requests, so that the actual implementation only need to
 * focus on extracting JWT tokens from HTTP header {@code Authorization} or from cookies.
 *
 * <p>In case of AJAX requests HTTP header {@code X-Requested-With} is set by the front-end and
 * the actual JWT token is sent through two cookies.
 *
 * <p>In case of API requests header {@code Authorization} with {@code Bearer} is used.
 *
 * @see AjaxRequestMatcher
 * @see JwtAuthenticationService
 * @see JwtAuthenticationTokenCookieResolver
 */
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final String BEARER_TOKEN_PREFIX = "Bearer";

  private static final String BEARER_TOKEN_ATTRIBUTE = "bearer_token";

  private static final RequestMatcher AJAX_REQUEST_MATCHER = new AjaxRequestMatcher();

  private static final RequestMatcher AUTHORIZATION_BEARER_REQUEST_HEADER_MATCHER =
      new AuthorizationBearerRequestHeaderMatcher();

  private static final AuthenticationSuccessHandler NOOP_AUTH_SUCCESS_HANDLER =
      (request, response, authentication) -> {
      };

  private final JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver;

  /**
   * Creates a new {@code JwtAuthenticationFilter}.
   *
   * @param requiresAuthenticationRequestMatcher a possibly (compound matcher) which indicates for
   *          which {@link HttpServletRequest} this filter must be activated.
   * @param authenticationManager An {@link AuthenticationManager} to delegate the authentication
   *          request to
   * @param authenticationFailureHandler An {@link AuthenticationFailureHandler} for processing
   *          failed authentication requests
   * @param authenticationTokenCookieResolver An {@link AuthenticationSuccessHandler} for processing
   *          successful authentication requests
   */
  public JwtAuthenticationFilter(
      RequestMatcher requiresAuthenticationRequestMatcher,
      AuthenticationManager authenticationManager,
      AuthenticationFailureHandler authenticationFailureHandler,
      JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver) {

    super(requiresAuthenticationRequestMatcher);

    setAuthenticationManager(authenticationManager);
    setAuthenticationSuccessHandler(NOOP_AUTH_SUCCESS_HANDLER);
    setAuthenticationFailureHandler(authenticationFailureHandler);

    this.authenticationTokenCookieResolver = authenticationTokenCookieResolver;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {

    Optional<String> bearerToken =
        Optional.ofNullable((String) request.getAttribute(BEARER_TOKEN_ATTRIBUTE));

    if (!bearerToken.isPresent() && AJAX_REQUEST_MATCHER.matches(request)) {
      bearerToken = authenticationTokenCookieResolver.resolveToken(request);
    }

    return bearerToken.map(JwtAuthenticationToken::of)
        .map(getAuthenticationManager()::authenticate)
        .orElse(null);
  }

  /**
   * {@code JwtAuthenticationFilter} required authentication in two cases. When HTTP header
   * {@code X-Requested-With} is present or when HTTP header {@code Authorization} with
   * {@code Bearer} token is sent; unless the super class' overridden method returns {@code false}.
   *
   * @param request HTTP request
   * @param response HTTP response
   * @return {@code true} if {@code JwtAuthenticationFilter} has to proceed with authentication the
   *          current request, false otherwise
   */
  @Override
  protected boolean requiresAuthentication(
      HttpServletRequest request, HttpServletResponse response) {

    if (!super.requiresAuthentication(request, response)) {
      return false;
    }

    if (AJAX_REQUEST_MATCHER.matches(request)) {
      return true;
    }

    return AUTHORIZATION_BEARER_REQUEST_HEADER_MATCHER.matches(request);
  }

  /**
   * Sets {@code authResult} to the current security context and proceeds the filter chain.
   *
   * @param request HTTP request
   * @param response HTTP response
   * @param chain filter chain
   * @param authResult authentication result
   *
   * @throws IOException Upon I/O errors
   * @throws ServletException Upon unrecoverable servlet errors
   */
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {

    // Set authentication here in the current security context
    super.successfulAuthentication(request, response, chain, authResult);

    // Continue filter chain after security context has been set
    chain.doFilter(request, response);
  }

  /**
   * Matches those HTTP requests where HTTP header {@code Authorization} with
   * {@code Bearer} token is sent.
   */
  private static class AuthorizationBearerRequestHeaderMatcher implements RequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {
      Optional<String> bearerToken = extractBearerToken(request);
      bearerToken.ifPresent(it -> request.setAttribute(BEARER_TOKEN_ATTRIBUTE, bearerToken.get()));
      return bearerToken.isPresent();
    }

    private Optional<String> extractBearerToken(HttpServletRequest request) {
      String authHeaderValue = request.getHeader(AUTHORIZATION_HEADER);
      if (StringUtils.isEmpty(authHeaderValue)) {
        log.debug("Authorization header is empty.");
        return Optional.empty();
      }

      if (!StringUtils.substringMatch(authHeaderValue, 0, BEARER_TOKEN_PREFIX)) {
        log.debug(
            "Token prefix {} in Authorization header was not found.",
            BEARER_TOKEN_PREFIX
        );

        return Optional.empty();
      }

      return Optional.of(authHeaderValue.substring(BEARER_TOKEN_PREFIX.length() + 1))
          .filter(StringUtils::hasText);
    }

  }

}
