package io.relinkr.core.web;

import static io.relinkr.test.Mocks.JWS_VISITOR_COOKIE_SECRET_KEY;

import java.time.Duration;

public class JwsCookieManagerTest extends AbstractCookieManagerTest {

    @Override
    CookieManager createCookieManager(String cookieName, Duration cookieMaxAgeDuration) {
        return new JwsCookieManager(
                cookieName, cookieMaxAgeDuration, true, JWS_VISITOR_COOKIE_SECRET_KEY
        );
    }

}
