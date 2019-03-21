package com.springuni.hermes.core.web;

import static com.springuni.hermes.Mocks.VISITOR_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.springuni.hermes.Mocks;
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

    private final CookieManager cookieManager = createCookieManager(COOKIE_NAME, COOKIE_MAX_AGE);

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
    }

    @Test
    public void givenCookieMaxAge_whenCreateCookieManager_thenCookieNameAndMaxAgeSet() {
        assertEquals(COOKIE_NAME, cookieManager.getCookieName());
        assertEquals(24 * 3600, cookieManager.getCookieMaxAge().intValue());
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

}
