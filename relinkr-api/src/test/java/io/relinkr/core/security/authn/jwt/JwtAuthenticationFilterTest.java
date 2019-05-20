package io.relinkr.core.security.authn.jwt;

import static io.relinkr.core.security.authn.jwt.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static io.relinkr.core.security.authn.jwt.JwtAuthenticationFilter.BEARER_TOKEN_PREFIX;
import static java.util.Collections.emptySet;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import io.relinkr.core.security.authn.user.UserIdAuthenticationToken;
import io.relinkr.core.web.AjaxRequestMatcher;
import io.relinkr.test.web.BaseFilterTest;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

@RunWith(MockitoJUnitRunner.Silent.class)
public class JwtAuthenticationFilterTest extends BaseFilterTest {

    private static final Authentication AUTHENTICATED_TOKEN =
            UserIdAuthenticationToken.of(1L, emptySet());

    private static final String PUBLIC_PATH = "/public";

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtAuthenticationTokenCookieResolver authenticationTokenCookieResolver;

    @Mock
    private SecurityContext securityContext;

    @Captor
    private ArgumentCaptor<JwtAuthenticationToken> jwtAuthenticationTokenCaptor;

    private JwtAuthenticationFilter jwtTokenFilter;

    @Before
    public void setUp() {
        super.setUp();

        SecurityContextHolder.setContext(securityContext);

        jwtTokenFilter = new JwtAuthenticationFilter(
                new NegatedRequestMatcher(new AntPathRequestMatcher(PUBLIC_PATH)),
                authenticationManager,
                (request, response, authentication) -> response.sendError(SC_UNAUTHORIZED),
                authenticationTokenCookieResolver
        );
    }

    // JWT Token is given in the Authorization header

    @Test
    public void givenValidJwtTokenInHeader_whenDoFilter_thenOkAndContextSet() throws Exception {
        request.addHeader(AUTHORIZATION_HEADER, BEARER_TOKEN_PREFIX + " valid");
        given(authenticationManager.authenticate(any(JwtAuthenticationToken.class)))
                .willReturn(AUTHENTICATED_TOKEN);

        jwtTokenFilter.doFilter(request, response, filterChain);

        assertEquals(SC_OK, response.getStatus());
        assertJwtAuthenticationToken("valid");

        then(securityContext).should().setAuthentication(AUTHENTICATED_TOKEN);
    }

    @Test
    public void givenValidJwtTokenInHeaderAndIgnoredPath_whenDoFilter_thenOkAndContextSet()
            throws Exception {

        request.addHeader(AUTHORIZATION_HEADER, BEARER_TOKEN_PREFIX + " valid");

        // TODO: In newer Mockito versions, there's a better way to enable lenient stubbing for a specific test method
        // Ref: http://blog.mockito.org/2018/07/new-mockito-api-lenient.html
        given(authenticationManager.authenticate(any(JwtAuthenticationToken.class)))
                .willReturn(AUTHENTICATED_TOKEN);

        request.setPathInfo(PUBLIC_PATH);
        jwtTokenFilter.doFilter(request, response, filterChain);

        assertEquals(SC_OK, response.getStatus());

        then(authenticationManager).shouldHaveZeroInteractions();
        then(securityContext).shouldHaveZeroInteractions();
    }

    @Test
    public void givenInvalidJwtTokenInHeader_whenDoFilter_thenUnauthorizedAndNoContextSet()
            throws Exception {

        request.addHeader(AUTHORIZATION_HEADER, BEARER_TOKEN_PREFIX + " invalid");
        given(authenticationManager.authenticate(any(JwtAuthenticationToken.class)))
                .willThrow(BadCredentialsException.class);

        jwtTokenFilter.doFilter(request, response, filterChain);

        assertEquals(SC_UNAUTHORIZED, response.getStatus());
        assertJwtAuthenticationToken("invalid");

        then(securityContext).shouldHaveZeroInteractions();
    }

    // JWT Token is given in two cookies + X-Requested-With is set

    @Test
    public void givenValidJwtTokenInCookie_whenDoFilter_thenOkAndContextSet() throws Exception {
        request.addHeader(
                AjaxRequestMatcher.X_REQUESTED_WITH_HEADER,
                AjaxRequestMatcher.X_REQUESTED_WITH_VALUE);

        given(authenticationTokenCookieResolver.resolveToken(request))
                .willReturn(Optional.of("valid"));

        given(authenticationManager.authenticate(any(JwtAuthenticationToken.class)))
                .willReturn(AUTHENTICATED_TOKEN);

        jwtTokenFilter.doFilter(request, response, filterChain);

        assertEquals(SC_OK, response.getStatus());
        assertJwtAuthenticationToken("valid");

        then(securityContext).should().setAuthentication(AUTHENTICATED_TOKEN);
    }

    @Test
    public void givenValidJwtTokenInCookieAndIgnoredPath_whenDoFilter_thenOkAndContextSet()
            throws Exception {

        request.addHeader(
                AjaxRequestMatcher.X_REQUESTED_WITH_HEADER,
                AjaxRequestMatcher.X_REQUESTED_WITH_VALUE);

        given(authenticationTokenCookieResolver.resolveToken(request))
                .willReturn(Optional.of("valid"));

        // TODO: In newer Mockito versions, there's a better way to enable lenient stubbing for a specific test method
        // Ref: http://blog.mockito.org/2018/07/new-mockito-api-lenient.html
        given(authenticationManager.authenticate(any(JwtAuthenticationToken.class)))
                .willReturn(AUTHENTICATED_TOKEN);

        request.setPathInfo(PUBLIC_PATH);
        jwtTokenFilter.doFilter(request, response, filterChain);

        assertEquals(SC_OK, response.getStatus());

        then(authenticationManager).shouldHaveZeroInteractions();
        then(securityContext).shouldHaveZeroInteractions();
    }

    @Test
    public void givenInvalidJwtTokenInCookie_whenDoFilter_thenUnauthorizedAndNoContextSet()
            throws Exception {

        request.addHeader(
                AjaxRequestMatcher.X_REQUESTED_WITH_HEADER,
                AjaxRequestMatcher.X_REQUESTED_WITH_VALUE);

        given(authenticationTokenCookieResolver.resolveToken(request))
                .willReturn(Optional.of("invalid"));

        given(authenticationManager.authenticate(any(JwtAuthenticationToken.class)))
                .willThrow(BadCredentialsException.class);

        jwtTokenFilter.doFilter(request, response, filterChain);

        assertEquals(SC_UNAUTHORIZED, response.getStatus());
        assertJwtAuthenticationToken("invalid");

        then(securityContext).shouldHaveZeroInteractions();
    }

    @Test
    public void givenNoJwtToken_whenDoFilter_thenFilterProceeds() throws Exception {
        jwtTokenFilter.doFilter(request, response, filterChain);

        then(authenticationManager).shouldHaveZeroInteractions();
        then(securityContext).shouldHaveZeroInteractions();
    }

    @Test
    public void givenNoJwtTokenAndIgnoredPath_whenDoFilter_thenFilterProceeds() throws Exception {
        request.setPathInfo(PUBLIC_PATH);
        jwtTokenFilter.doFilter(request, response, filterChain);

        then(authenticationManager).shouldHaveZeroInteractions();
        then(securityContext).shouldHaveZeroInteractions();
    }

    private void assertJwtAuthenticationToken(String expectedTokenValue) {
        then(authenticationManager).should().authenticate(jwtAuthenticationTokenCaptor.capture());
        JwtAuthenticationToken jwtAuthenticationToken = jwtAuthenticationTokenCaptor.getValue();
        assertEquals(expectedTokenValue, jwtAuthenticationToken.getPrincipal());
    }

}