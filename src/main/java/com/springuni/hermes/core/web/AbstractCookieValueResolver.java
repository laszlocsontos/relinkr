package com.springuni.hermes.core.web;

import java.time.Duration;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public abstract class AbstractCookieValueResolver<V>
        implements CookieValueResolver<V>, EnvironmentAware, InitializingBean {

    private final String cookieName;
    private final Duration cookieMaxAgeDuration;
    private final String secretKeyProperty;

    private CookieManager cookieManager;
    private Environment environment;


    public AbstractCookieValueResolver(
            String cookieName, Duration cookieMaxAgeDuration, String secretKeyProperty) {

        this.cookieName = cookieName;
        this.cookieMaxAgeDuration = cookieMaxAgeDuration;
        this.secretKeyProperty = secretKeyProperty;
    }

    @Override
    public final Optional<V> resolveValue(HttpServletRequest request) {
        return cookieManager.getCookie(request).map(this::fromString);
    }

    @Override
    public final void setValue(HttpServletResponse response, V value) {
        Optional<String> cookieValue = Optional.ofNullable(value).map(this::toString);
        if (cookieValue.isPresent()) {
            cookieManager.addCookie(response, cookieValue.get());
            return;
        }

        cookieManager.removeCookie(response);
    }

    protected abstract V fromString(String value);

    protected abstract String toString(V value);

    @Override
    public final void afterPropertiesSet() {
        String secretKey = environment.getRequiredProperty(secretKeyProperty);
        cookieManager = new JwsCookieManager(cookieName, cookieMaxAgeDuration, secretKey);
    }

    @Override
    public final void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
