package io.relinkr.core.web;

import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AjaxRequestMatcher implements RequestMatcher {

  public static final String X_REQUESTED_WITH_HEADER = "X-Requested-With";
  public static final String X_REQUESTED_WITH_VALUE = "XMLHttpRequest";

  private final RequestMatcher delegate =
      new RequestHeaderRequestMatcher(X_REQUESTED_WITH_HEADER, X_REQUESTED_WITH_VALUE);

  @Override
  public boolean matches(@NonNull HttpServletRequest request) {
    return delegate.matches(request);
  }

}
