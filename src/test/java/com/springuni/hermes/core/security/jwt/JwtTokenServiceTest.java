package com.springuni.hermes.core.security.jwt;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.springuni.hermes.core.util.IdentityGenerator;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.NonceExpiredException;

public class JwtTokenServiceTest {

    private static final String JWT_TOKEN_VALID =
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIzNDU0MzUzMjQ1MzQ1MyIsInN1YiI6IjUzMjQ1"
                    + "MzQ1MzQ1MzQ1IiwiZXhwIjoyNTM0MDIyMTQ0MDAsImlhdCI6MTUxNjIzOTAyMiwiYXV0aG9yaXR"
                    + "pZXMiOiJVU0VSIn0.xTT81NHZdRdo-Enk5Dfl-v90wYcbF5sbHCKmda5yTB8n5kZ3Y-VhykVGXn"
                    + "VmfgsPXPgO6QmmpIky1vPQYRUHsw";

    private static final String JWT_TOKEN_INVALID =
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIzNDU0MzUzMjQ1MzQ1MyIsInN1YiI6IjUzMjQ1"
                    + "MzQ1MzQ1MzQ1IiwiZXhwIjoyNTM0MDIyMTQ0MDAsImlhdCI6MTUxNjIzOTAyMiwiYXV0aG9yaXR"
                    + "pZXMiOiJVU0VSIn0.B_m-1j9SqmjrcyHwyMMKsxdBi9qLe2akpfZXq4VPG73ppuJXCuB6GPvDvH"
                    + "GeMqLQkCA1Al7iBu1oGU7i5QHc5A";

    private static final String JWT_TOKEN_EXPIRED =
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIzNDU0MzUzMjQ1MzQ1MyIsInN1YiI6IjUzMjQ1"
                    + "MzQ1MzQ1MzQ1IiwiZXhwIjoxNTE0Njc4NDAwLCJpYXQiOjE1MTYyMzkwMjIsImF1dGhvcml0aWV"
                    + "zIjoiVVNFUiJ9.tJYq8hIbSc2gSKNU2F4c29Nn6X1L4HgEPnqS8MIEDEMN0nCnYLNqo_yWStemV"
                    + "vcOV0YVKWvZCey3KspAhEXQKA";

    private static final String SECRET_KEY =
            "cFZJY3VpV2RMZHZMQTdVNzRAMVUqc2RFWTJoSlNpJk5MNzE2TkghI1FqKnEmKjk2TjY4TnZ5MG9t";

    private JwtTokenService jwtTokenService;

    @Before
    public void setUp() throws Exception {
        jwtTokenService = new JwtTokenServiceImpl(SECRET_KEY, IdentityGenerator.getInstance());
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
