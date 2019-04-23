package com.springuni.hermes.core.web;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieValueResolver<V> {

    Optional<V> resolveValue(HttpServletRequest request);

    void setValue(HttpServletResponse response, V value);

}
