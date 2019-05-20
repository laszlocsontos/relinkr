package com.springuni.hermes.core.web;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractCookieValueResolver<V> implements CookieValueResolver<V> {

    @Override
    public Optional<V> resolveValue(HttpServletRequest request) {
        return getCookieManager().getCookie(request).flatMap(this::fromString);
    }

    @Override
    public void setValue(HttpServletResponse response, V value) {
        Optional<String> cookieValue = Optional.ofNullable(value).flatMap(this::toString);
        if (cookieValue.isPresent()) {
            getCookieManager().addCookie(response, cookieValue.get());
            return;
        }

        getCookieManager().removeCookie(response);
    }

    protected abstract Optional<V> fromString(String value);

    protected abstract Optional<String> toString(V value);

    protected abstract CookieManager getCookieManager();

}
