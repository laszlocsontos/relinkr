package com.springuni.hermes.visitor.web;

import static com.springuni.hermes.test.Mocks.JWS_VISITOR_COOKIE_SECRET_KEY;
import static com.springuni.hermes.test.Mocks.JWS_VISITOR_COOKIE_VALUE;
import static com.springuni.hermes.test.Mocks.VISITOR_ID;
import static com.springuni.hermes.visitor.web.VisitorIdResolverImpl.COOKIE_MAX_AGE;
import static com.springuni.hermes.visitor.web.VisitorIdResolverImpl.COOKIE_NAME;
import static com.springuni.hermes.visitor.web.VisitorIdResolverImpl.VISITOR_SECRET_KEY_PROPERTY;
import static java.time.Duration.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.springuni.hermes.core.convert.EntityClassAwareIdToStringConverter;
import com.springuni.hermes.core.convert.StringToEntityClassAwareIdConverter;
import com.springuni.hermes.visitor.model.VisitorId;
import java.time.Duration;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.StringUtils;

public class VisitorIdResolverTest {

    private ConfigurableConversionService conversionService;
    private MockEnvironment environment;
    private VisitorIdResolver visitorIdResolver;

    @Before
    public void setUp() {
        conversionService = new GenericConversionService();

        conversionService.addConverter(
                String.class,
                VisitorId.class,
                new StringToEntityClassAwareIdConverter<>(VisitorId.class)
        );

        conversionService.addConverter(new EntityClassAwareIdToStringConverter<VisitorId>());

        environment = new MockEnvironment();
        environment.setProperty(VISITOR_SECRET_KEY_PROPERTY, JWS_VISITOR_COOKIE_SECRET_KEY);

        visitorIdResolver = new VisitorIdResolverImpl(conversionService, environment);
    }

    @Test
    public void givenNoVisitorCookie_whenResolveVisitorId_thenAbsent() {
        HttpServletRequest request = createMockHttpServletRequest(null);
        assertFalse(visitorIdResolver.resolveVisitorId(request).isPresent());
    }

    @Test
    public void givenValidVisitorCookie_whenResolveVisitorId_thenPresent() {
        HttpServletRequest request = createMockHttpServletRequest(JWS_VISITOR_COOKIE_VALUE);
        assertEquals(VISITOR_ID, visitorIdResolver.resolveVisitorId(request).orElse(null));
    }

    @Test
    public void givenInvalidVisitorCookie_whenResolveVisitorId_thenPresent() {
        HttpServletRequest request = createMockHttpServletRequest("bad");
        assertFalse(visitorIdResolver.resolveVisitorId(request).isPresent());
    }

    @Test
    public void givenNullVisitorIdCookie_whenSetVisitorId_thenEmptyCookieSent() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        visitorIdResolver.setVisitorId(response, null);
        assertCookie(response, "", ZERO);
    }

    @Test
    public void givenNonNullVisitorIdCookie_whenSetVisitorId_thenSignedCookieSent() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        visitorIdResolver.setVisitorId(response, VISITOR_ID);
        assertCookie(response, JWS_VISITOR_COOKIE_VALUE, COOKIE_MAX_AGE);
    }

    private HttpServletRequest createMockHttpServletRequest(String cookieValue) {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Optional.ofNullable(cookieValue)
                .filter(StringUtils::hasText)
                .ifPresent(it -> request.setCookies(new Cookie(COOKIE_NAME, cookieValue)));

        return request;
    }

    private void assertCookie(
            MockHttpServletResponse response, String expectedCookieValue, Duration expectedMaxAge) {

        Cookie cookie = response.getCookie(COOKIE_NAME);

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
