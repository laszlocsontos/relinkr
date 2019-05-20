package com.springuni.hermes.core.web;

import java.time.Duration;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

public class CookieManager extends CookieGenerator {

    public static final int MAX_AGE_AUTO_EXPIRE = -1;

    public CookieManager(String cookieName, Duration cookieMaxAgeDuration, boolean httpOnly) {
        setCookieName(cookieName);
        setCookieMaxAge(cookieMaxAgeDuration);
        setCookieHttpOnly(httpOnly);
        setCookieSecure(true);
    }

    public Optional<String> getCookie(HttpServletRequest request) {
        return Optional.ofNullable(getCookieName())
                .map(it -> WebUtils.getCookie(request, it))
                .map(Cookie::getValue)
                .filter(StringUtils::hasText);
    }

    public void setCookieMaxAge(Duration cookieMaxAgeDuration) {
        int cookieMaxAge = Optional.ofNullable(cookieMaxAgeDuration)
                .map(Duration::getSeconds)
                .map(Long::intValue)
                .orElse(MAX_AGE_AUTO_EXPIRE);

        setCookieMaxAge(cookieMaxAge);
    }

    @Override
    public final void setCookieSecure(boolean cookieSecure) {
        Assert.isTrue(cookieSecure, "Cookie cannot be insecure");
        super.setCookieSecure(cookieSecure);
    }

}
