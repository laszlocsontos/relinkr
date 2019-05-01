package com.springuni.hermes.core.web;

import static com.springuni.hermes.core.web.CookieManager.MAX_AGE_AUTO_EXPIRE;
import static com.springuni.hermes.test.Mocks.VISITOR_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.Duration;
import javax.servlet.http.Cookie;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public abstract class AbstractCookieManagerTest {

    private static final String COOKIE_NAME = "test_cookie";
    private static final String COOKIE_VALUE = String.valueOf(VISITOR_ID);
    private static final Duration COOKIE_MAX_AGE = Duration.ofHours(24);

    private CookieManager cookieManager;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp() {
        request = new MockHttpServletRequest();

        response = new MockHttpServletResponse() {

            @Override
            public void addCookie(Cookie cookie) {
                super.addCookie(cookie);
                request.setCookies(cookie);
            }


        };

        cookieManager = createCookieManager(COOKIE_NAME, COOKIE_MAX_AGE);
    }

    @Test
    public void givenCookieNameAndMaxAge_whenCreateCookieManager_thenCookieNameAndMaxAgeSet() {
        assertCookie(COOKIE_NAME, COOKIE_MAX_AGE.getSeconds(), true, true, cookieManager);
    }

    @Test
    public void givenCookieNameAndNoMaxAge_whenCreateCookieManager_thenCookieNameAndMaxAgeSet() {
        cookieManager = createCookieManager(COOKIE_NAME, null);
        assertCookie(COOKIE_NAME, MAX_AGE_AUTO_EXPIRE, true, true, cookieManager);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenFalse_whenSetCookieSecure_thenIllegalArgumentException() {
        cookieManager.setCookieSecure(false);
    }

    @Test
    public void givenCookieAdded_whenGetCookie_thenValuePresent() {
        cookieManager.addCookie(response, COOKIE_VALUE);
        assertEquals(COOKIE_VALUE, cookieManager.getCookie(request).orElse(null));
    }

    @Test
    public void givenCookieRemoved_whenGetCookie_thenValueAbsent() {
        cookieManager.addCookie(response, COOKIE_VALUE);
        cookieManager.removeCookie(response);
        assertFalse(cookieManager.getCookie(request).isPresent());
    }

    abstract CookieManager createCookieManager(String cookieName, Duration cookieMaxAgeDuration);

    private void assertCookie(
            String expectedCookieName,
            long expectCookieMaxAge,
            boolean expectedHttpOnly,
            boolean expectedSecure,
            CookieManager cookieManager) {

        assertEquals(expectedCookieName, cookieManager.getCookieName());
        assertEquals(expectCookieMaxAge, cookieManager.getCookieMaxAge().longValue());
        assertEquals(expectedHttpOnly, cookieManager.isCookieHttpOnly());
        assertEquals(expectedSecure, cookieManager.isCookieSecure());
    }

}
