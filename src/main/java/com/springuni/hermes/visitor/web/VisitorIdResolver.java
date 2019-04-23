package com.springuni.hermes.visitor.web;

import com.springuni.hermes.core.web.CookieValueResolver;
import com.springuni.hermes.visitor.model.VisitorId;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VisitorIdResolver extends CookieValueResolver<VisitorId> {

    default Optional<VisitorId> resolveVisitorId(HttpServletRequest request) {
        return resolveValue(request);
    }

    default void setVisitorId(HttpServletResponse response, VisitorId visitorId) {
        setValue(response, visitorId);
    }

}
