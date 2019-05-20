package com.springuni.hermes.core.web;

import java.time.Duration;

public class CookieManagerTest extends AbstractCookieManagerTest {

    @Override
    CookieManager createCookieManager(String cookieName, Duration cookieMaxAgeDuration) {
        return new CookieManager(cookieName, cookieMaxAgeDuration, true);
    }

}
