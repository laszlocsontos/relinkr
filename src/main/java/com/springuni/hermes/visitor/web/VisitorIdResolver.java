package com.springuni.hermes.visitor.web;

import com.springuni.hermes.visitor.model.VisitorId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VisitorIdResolver {

    VisitorId resolveVisitorId(HttpServletRequest request);

    void setVisitorId(HttpServletRequest request, HttpServletResponse response, VisitorId visitorId);

}
