package com.springuni.hermes.core.web;

import java.time.Duration;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

public class CookieManager extends CookieGenerator {

    public CookieManager(String cookieName, Duration cookieMaxAgeDuration) {
        setCookieName(cookieName);

        Integer cookieMaxAge = Optional.ofNullable(cookieMaxAgeDuration)
                .map(Duration::getSeconds)
                .map(Long::intValue)
                .orElse(null);

        setCookieMaxAge(cookieMaxAge);
    }

    public Optional<String> getCookie(HttpServletRequest request) {
        return Optional.ofNullable(getCookieName())
                .map(it -> WebUtils.getCookie(request, it))
                .map(Cookie::getValue)
                .filter(StringUtils::hasText);
    }

}
