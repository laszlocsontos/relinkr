package com.springuni.hermes.core.web;

import static java.time.Duration.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.Duration;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.StringUtils;

public abstract class AbstractCookieValueResolverTest<V> {

    private final String cookieName;
    private final Duration cookieMaxAge;
    private final V originalCookieValue;
    private final String sentCookieValue;

    private MockEnvironment environment;
    private AbstractCookieValueResolver<V> cookieValueResolver;

    public AbstractCookieValueResolverTest(
            String cookieName, Duration cookieMaxAge,
            V originalCookieValue, String sentCookieValue) {

        this.cookieName = cookieName;
        this.cookieMaxAge = cookieMaxAge;
        this.originalCookieValue = originalCookieValue;
        this.sentCookieValue = sentCookieValue;
    }

    @Before
    public void setUp() {
        environment = new MockEnvironment();
        setUpEnvironment(environment);

        cookieValueResolver = createCookieValueResolver(environment);
    }

    protected abstract void setUpEnvironment(MockEnvironment environment);

    protected abstract AbstractCookieValueResolver<V> createCookieValueResolver(
            Environment environment);

    @Test
    public void givenNoCookie_whenResolveValue_thenAbsent() {
        HttpServletRequest request = createMockHttpServletRequest(null);
        assertFalse(cookieValueResolver.resolveValue(request).isPresent());
    }

    @Test
    public void givenValidCookie_whenResolveValue_thenPresent() {
        HttpServletRequest request = createMockHttpServletRequest(sentCookieValue);
        assertCookieValue(originalCookieValue,
                cookieValueResolver.resolveValue(request).orElse(null));
    }

    protected void assertCookieValue(V expected, V actual) {
        assertEquals(expected, actual);
    }

    @Test
    public void givenInvalidCookie_whenResolveValue_thenAbsent() {
        HttpServletRequest request = createMockHttpServletRequest("bad");
        assertFalse(cookieValueResolver.resolveValue(request).isPresent());
    }

    @Test
    public void givenNullCookie_whenSetValue_thenEmptyCookieSent() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        cookieValueResolver.setValue(response, null);
        assertCookie(response, "", ZERO);
    }

    @Test
    public void givenNonNullCookie_whenSetValue_thenSignedCookieSent() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        cookieValueResolver.setValue(response, originalCookieValue);
        assertCookie(response, sentCookieValue, cookieMaxAge);
    }

    private HttpServletRequest createMockHttpServletRequest(String cookieValue) {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Optional.ofNullable(cookieValue)
                .filter(StringUtils::hasText)
                .ifPresent(it -> request.setCookies(new Cookie(cookieName, cookieValue)));

        return request;
    }

    private void assertCookie(
            MockHttpServletResponse response, String expectedCookieValue, Duration expectedMaxAge) {

        Cookie cookie = response.getCookie(cookieName);

        assertEquals(
                expectedCookieValue,
                Optional.ofNullable(cookie).map(Cookie::getValue).orElse(null)
        );

        assertEquals(
                expectedMaxAge,
                Optional.ofNullable(cookie)
                        .map(Cookie::getMaxAge).map(Duration::ofSeconds).orElse(null)
        );
    }

}
