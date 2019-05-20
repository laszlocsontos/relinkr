package io.relinkr.core.security.authn.jwt;

import static io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolverImpl.TOKEN_PAYLOAD_COOKIE_NAME;
import static io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolverImpl.TOKEN_SIGNATURE_COOKIE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import io.relinkr.test.web.BaseServletTest;
import io.relinkr.core.web.CookieManager;
import java.util.Optional;
import javax.servlet.http.Cookie;
import org.junit.Test;
import org.springframework.util.StringUtils;

public class JwtAuthenticationTokenCookieResolverTest extends BaseServletTest {

    private static final String JWT_SIGNATURE_PART =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    private static final String JWT_PAYLOAD_PART =
            "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ";

    private static final String JWT_FULL =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                    + "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ."
                    + "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    private final JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver =
            new JwtAuthenticationTokenCookieResolverImpl();

    @Test
    public void givenNoToken_whenResolveToken_thenValueAbsent() {
        assertFalse(authenticationTokenCookieResolver.resolveToken(request).isPresent());
    }

    @Test
    public void givenNoToken_whenSetToken_thenCookiesSet() {
        authenticationTokenCookieResolver.setToken(response, JWT_FULL);

        assertCookieValuesPresent(JWT_PAYLOAD_PART, JWT_SIGNATURE_PART);
    }

    @Test
    public void givenNoToken_whenRemoveToken_thenCookieValuesAbsent() {
        authenticationTokenCookieResolver.removeToken(response);

        assertCookieValuesAbsent();
    }

    @Test
    public void givenInvalidTokenWithoutDelimiter_whenResolveToken_thenValuePresent() {
        request.setCookies(
                new Cookie(TOKEN_PAYLOAD_COOKIE_NAME, JWT_PAYLOAD_PART),
                new Cookie(TOKEN_SIGNATURE_COOKIE_NAME, "bad")
        );

        assertFalse(authenticationTokenCookieResolver.resolveToken(request).isPresent());
    }

    @Test
    public void givenInvalidTokenWithDelimiterAtStart_whenResolveToken_thenValuePresent() {
        request.setCookies(
                new Cookie(TOKEN_PAYLOAD_COOKIE_NAME, JWT_PAYLOAD_PART),
                new Cookie(TOKEN_SIGNATURE_COOKIE_NAME, ".bad")
        );

        assertFalse(authenticationTokenCookieResolver.resolveToken(request).isPresent());
    }

    @Test
    public void givenInvalidTokenWithDelimiterAtEnd_whenResolveToken_thenValuePresent() {
        request.setCookies(
                new Cookie(TOKEN_PAYLOAD_COOKIE_NAME, JWT_PAYLOAD_PART),
                new Cookie(TOKEN_SIGNATURE_COOKIE_NAME, "bad.")
        );

        assertFalse(authenticationTokenCookieResolver.resolveToken(request).isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenInvalidToken_whenSetToken_thenIllegalArgumentException() {
        authenticationTokenCookieResolver.setToken(response, "bad");
    }

    @Test
    public void givenValidToken_whenResolveToken_thenValueAbsent() {
        request.setCookies(
                new Cookie(TOKEN_PAYLOAD_COOKIE_NAME, JWT_PAYLOAD_PART),
                new Cookie(TOKEN_SIGNATURE_COOKIE_NAME, JWT_SIGNATURE_PART)
        );

        assertTrue(authenticationTokenCookieResolver.resolveToken(request).isPresent());
    }

    @Test
    public void givenValidToken_whenSetToken_thenCookiesReplaced() {
        request.setCookies(
                new Cookie(TOKEN_PAYLOAD_COOKIE_NAME, JWT_PAYLOAD_PART),
                new Cookie(TOKEN_SIGNATURE_COOKIE_NAME, JWT_SIGNATURE_PART)
        );

        authenticationTokenCookieResolver.setToken(response, "aa.bb.cc");

        assertCookieValuesPresent("bb", "aa.cc");
    }

    @Test
    public void givenValidToken_whenRemoveToken_thenCookieValuesAbsent() {
        request.setCookies(
                new Cookie(TOKEN_PAYLOAD_COOKIE_NAME, JWT_PAYLOAD_PART),
                new Cookie(TOKEN_SIGNATURE_COOKIE_NAME, JWT_SIGNATURE_PART)
        );

        authenticationTokenCookieResolver.removeToken(response);

        assertCookieValuesAbsent();
    }

    private void assertCookieValuesAbsent() {
        Cookie payloadCookie = response.getCookie(TOKEN_PAYLOAD_COOKIE_NAME);

        assertNull(Optional.ofNullable(payloadCookie)
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .orElse(null)
        );

        Cookie signatureCookie = response.getCookie(TOKEN_SIGNATURE_COOKIE_NAME);

        assertNull(Optional.ofNullable(signatureCookie)
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .orElse(null)
        );
    }

    private void assertCookieValuesPresent(String expectedPayload, String expectedSignature) {
        Cookie payloadCookie = response.getCookie(TOKEN_PAYLOAD_COOKIE_NAME);

        assertEquals(
                expectedPayload,
                Optional.ofNullable(payloadCookie).map(Cookie::getValue).orElse(null)
        );

        assertEquals(
                CookieManager.MAX_AGE_AUTO_EXPIRE,
                Optional.ofNullable(payloadCookie).map(Cookie::getMaxAge).orElse(0).intValue()
        );

        Cookie signatureCookie = response.getCookie(TOKEN_SIGNATURE_COOKIE_NAME);

        assertEquals(
                expectedSignature,
                Optional.ofNullable(signatureCookie).map(Cookie::getValue).orElse(null)
        );

        assertEquals(
                CookieManager.MAX_AGE_AUTO_EXPIRE,
                Optional.ofNullable(signatureCookie).map(Cookie::getMaxAge).orElse(0).intValue()
        );
    }

}
