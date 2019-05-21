package io.relinkr.core.security.authn;

import static io.relinkr.core.security.authn.jwt.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static io.relinkr.core.security.authn.jwt.JwtAuthenticationFilter.BEARER_TOKEN_PREFIX;
import static io.relinkr.test.Mocks.JWT_TOKEN_EXPIRED;
import static io.relinkr.test.Mocks.JWT_TOKEN_VALID;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import org.junit.Test;

public class WebRequestAuthorizationWithJwtTest extends AbstractWebRequestAuthorizationTest {

  @Test
  public void givenInvalidAuthentication_whenRootAccessed_thenOk() throws Exception {
    super.givenInvalidAuthentication_whenRootAccessed_thenStatus(SC_OK);
  }

  @Test
  public void givenInvalidAuthentication_whenShortLinkAccessed_thenOk()
      throws Exception {

    super.givenInvalidAuthentication_whenShortLinkAccessed_thenStatus(SC_OK);
  }

  @Test
  public void givenInvalidAuthentication_whenApiAccessed_thenUnauthorized() throws Exception {
    super.givenInvalidAuthentication_whenApiAccessed_thenStatus(SC_UNAUTHORIZED);
  }

  @Override
  protected void withValidAuthentication() {
    setAuthorizationHeader(JWT_TOKEN_VALID);
  }

  @Override
  protected void withInvalidAuthentication() {
    setAuthorizationHeader(JWT_TOKEN_EXPIRED);
  }

  private void setAuthorizationHeader(String value) {
    addHttpHeader(AUTHORIZATION_HEADER, BEARER_TOKEN_PREFIX + " " + value);
  }

}
