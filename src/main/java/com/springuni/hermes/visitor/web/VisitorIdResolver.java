package com.springuni.hermes.visitor.web;

import com.springuni.hermes.visitor.model.VisitorId;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VisitorIdResolver {

    Optional<VisitorId> resolveVisitorId(HttpServletRequest request);

    void setVisitorId(HttpServletResponse response, VisitorId visitorId);

}
