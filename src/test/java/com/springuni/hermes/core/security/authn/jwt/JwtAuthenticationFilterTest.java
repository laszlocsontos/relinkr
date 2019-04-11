package com.springuni.hermes.core.security.authn.jwt;

import static com.springuni.hermes.core.security.authn.jwt.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static com.springuni.hermes.core.security.authn.jwt.JwtAuthenticationFilter.TOKEN_PREFIX;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.core.context.SecurityContextHolder.MODE_GLOBAL;

import com.springuni.hermes.test.web.BaseFilterTest;
import javax.servlet.Filter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterTest extends BaseFilterTest {

    @Mock
    private Authentication authentication;

    @Mock
    private JwtAuthenticationService jwtAuthenticationService;

    @Mock
    private SecurityContext securityContext;

    private Filter jwtTokenFilter;


    @Before
    public void setUp() throws Exception {
        super.setUp();

        SecurityContextHolder.setStrategyName(MODE_GLOBAL);
        SecurityContextHolder.setContext(securityContext);

        jwtTokenFilter = new JwtAuthenticationFilter(jwtAuthenticationService);
    }

    @Test
    public void givenValidJwtToken_doFilter_thenOkAndContextSet() throws Exception {
        request.addHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX + " valid");
        given(jwtAuthenticationService.parseJwtToken(anyString())).willReturn(authentication);

        jwtTokenFilter.doFilter(request, response, filterChain);

        assertEquals(SC_OK, response.getStatus());
        then(securityContext).should().setAuthentication(authentication);
    }

    @Test
    public void givenInvalidJwtToken_whenDoFilter_thenOkAndNoContextSet() throws Exception {
        request.addHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX + " invalid");
        given(jwtAuthenticationService.parseJwtToken(anyString())).willThrow(BadCredentialsException.class);

        jwtTokenFilter.doFilter(request, response, filterChain);

        assertEquals(SC_OK, response.getStatus());
        then(securityContext).shouldHaveZeroInteractions();
    }

    @Test
    public void givenNoJwtToken_whenDoFilter_thenOkAndNoContextSet() throws Exception {
        jwtTokenFilter.doFilter(request, response, filterChain);

        assertEquals(SC_OK, response.getStatus());
        then(securityContext).shouldHaveZeroInteractions();
    }

}
