package com.springuni.hermes.core.web;

import static com.springuni.hermes.test.Mocks.JWS_VISITOR_COOKIE_SECRET_KEY;

import java.time.Duration;

public class JwsCookieManagerTest extends AbstractCookieManagerTest {

    @Override
    CookieManager createCookieManager(String cookieName, Duration cookieMaxAgeDuration) {
        return new JwsCookieManager(
                cookieName, cookieMaxAgeDuration, JWS_VISITOR_COOKIE_SECRET_KEY
        );
    }

}
