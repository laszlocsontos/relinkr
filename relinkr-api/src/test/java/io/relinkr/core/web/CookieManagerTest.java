package io.relinkr.core.web;

import java.time.Duration;

public class CookieManagerTest extends AbstractCookieManagerTest {

    @Override
    CookieManager createCookieManager(String cookieName, Duration cookieMaxAgeDuration) {
        return new CookieManager(cookieName, cookieMaxAgeDuration, true);
    }

}
