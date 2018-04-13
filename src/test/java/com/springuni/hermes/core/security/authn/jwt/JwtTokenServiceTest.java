package com.springuni.hermes.core.security.authn.jwt;

import static com.springuni.hermes.Mocks.JWT_SECRET_KEY;
import static com.springuni.hermes.Mocks.JWT_TOKEN_EXPIRED;
import static com.springuni.hermes.Mocks.JWT_TOKEN_INVALID;
import static com.springuni.hermes.Mocks.JWT_TOKEN_VALID;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.springuni.hermes.core.util.IdentityGenerator;
import java.util.Arrays;
import java.util.Base64;
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
        jwtTokenService = new JwtTokenServiceImpl(
                Base64.getDecoder().decode(JWT_SECRET_KEY), IdentityGenerator.getInstance()
        );
    }

    @Test(expected = BadCredentialsException.class)
    public void createJwtToken_withInvalidPrincipal() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "invalid",
                        null,
                        Arrays.asList(new SimpleGrantedAuthority("USER"))
                );

        String jwtToken = jwtTokenService.createJwtToken(authentication, 1);
        jwtTokenService.parseJwtToken(jwtToken);
    }

    @Test
    public void createJwtToken_withValidPrincipal() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "53245345345345",
                        null,
                        Arrays.asList(new SimpleGrantedAuthority("USER"))
                );

        String jwtToken = jwtTokenService.createJwtToken(authentication, 1);
        authentication = jwtTokenService.parseJwtToken(jwtToken);

        assertEquals("53245345345345", authentication.getName());
        assertThat(authentication.getAuthorities(), contains(new SimpleGrantedAuthority("USER")));
    }

    @Test
    public void parseJwtToken_withValid() {
        Authentication authentication = jwtTokenService.parseJwtToken(JWT_TOKEN_VALID);
        assertEquals("53245345345345", authentication.getName());
        assertThat(authentication.getAuthorities(), contains(new SimpleGrantedAuthority("USER")));
        assertTrue(authentication.isAuthenticated());
    }

    @Test(expected = BadCredentialsException.class)
    public void parseJwtToken_withInvalid() {
        jwtTokenService.parseJwtToken(JWT_TOKEN_INVALID);
    }

    @Test(expected = NonceExpiredException.class)
    public void parseJwtToken_withExpired() {
        jwtTokenService.parseJwtToken(JWT_TOKEN_EXPIRED);
    }

}
