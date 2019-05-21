package io.relinkr.visitor.web;

import io.relinkr.core.web.CookieValueResolver;
import io.relinkr.visitor.model.VisitorId;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VisitorIdCookieResolver extends CookieValueResolver<VisitorId> {

  default Optional<VisitorId> resolveVisitorId(HttpServletRequest request) {
    return resolveValue(request);
  }

  default void setVisitorId(HttpServletResponse response, VisitorId visitorId) {
    setValue(response, visitorId);
  }

}
