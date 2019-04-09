package com.springuni.hermes.core.security.authn.jwt;

import static com.springuni.hermes.test.Mocks.JWT_TOKEN_EXPIRED;
import static com.springuni.hermes.test.Mocks.JWT_TOKEN_INVALID;
import static com.springuni.hermes.test.Mocks.JWT_TOKEN_VALID;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.springuni.hermes.core.util.IdentityGenerator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.NonceExpiredException;

public class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    @Before
    public void setUp() throws Exception {
        // TODO: Add private and public keys here
        jwtTokenService = new JwtTokenServiceImpl(null, null, IdentityGenerator.getInstance());
    }

    @Test(expected = BadCredentialsException.class)
    public void givenInvalidPrincipal_whenCreateJwtToken_thenBadCredentialsException() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "invalid",
                        null,
                        singletonList(new SimpleGrantedAuthority("USER"))
                );

        String jwtToken = jwtTokenService.createJwtToken(authentication, 1);
        jwtTokenService.parseJwtToken(jwtToken);
    }

    @Test
    public void givenValidPrincipal_whenCreateJwtToken_thenParsed() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "53245345345345",
                        null,
                        singletonList(new SimpleGrantedAuthority("USER"))
                );

        String jwtToken = jwtTokenService.createJwtToken(authentication, 1);
        authentication = jwtTokenService.parseJwtToken(jwtToken);

        assertEquals("53245345345345", authentication.getName());
        assertThat(authentication.getAuthorities(), contains(new SimpleGrantedAuthority("USER")));
    }

    @Test
    public void givenValidPrincipal_whenParseJwtToken_thenAuthenticated() {
        Authentication authentication = jwtTokenService.parseJwtToken(JWT_TOKEN_VALID);
        assertEquals("53245345345345", authentication.getName());
        assertThat(authentication.getAuthorities(), contains(new SimpleGrantedAuthority("USER")));
        assertTrue(authentication.isAuthenticated());
    }

    @Test(expected = BadCredentialsException.class)
    public void givenInvalidPrincipal_whenParseJwtToken_thenBadCredentialsException() {
        jwtTokenService.parseJwtToken(JWT_TOKEN_INVALID);
    }

    @Test(expected = NonceExpiredException.class)
    public void givenExpiredPrincipal_whenParseJwtToken_thenNonceExpiredException() {
        jwtTokenService.parseJwtToken(JWT_TOKEN_EXPIRED);
    }

}
