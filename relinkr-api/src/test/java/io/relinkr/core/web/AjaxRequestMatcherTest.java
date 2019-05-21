package io.relinkr.core.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.relinkr.test.web.BaseServletTest;
import org.junit.Test;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AjaxRequestMatcherTest extends BaseServletTest {

  private final RequestMatcher requestMatcher = new AjaxRequestMatcher();

  @Test(expected = IllegalArgumentException.class)
  public void givenNullHttpRequest_whenMatches_thenIllegalArgumentException() {
    requestMatcher.matches(null);
  }

  @Test
  public void givenNoHeader_whenMatches_thenFalse() {
    assertFalse(requestMatcher.matches(request));
  }

  @Test
  public void givenHeaderWithInvalidValue_whenMatches_thenFalse() {
    request.addHeader(AjaxRequestMatcher.X_REQUESTED_WITH_HEADER, "foo");
    assertFalse(requestMatcher.matches(request));
  }

  @Test
  public void givenHeaderWithValidValue_whenMatches_thenTrue() {
    request.addHeader(
        AjaxRequestMatcher.X_REQUESTED_WITH_HEADER,
        AjaxRequestMatcher.X_REQUESTED_WITH_VALUE);
    assertTrue(requestMatcher.matches(request));
  }

}
