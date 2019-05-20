package io.relinkr.core.security.authn.jwt;

import static io.relinkr.test.Mocks.JWT_TOKEN_EXPIRED;
import static io.relinkr.test.Mocks.JWT_TOKEN_INVALID;
import static io.relinkr.test.Mocks.JWT_TOKEN_VALID;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = JwtConfig.class)
public class JwtAuthenticationServiceTest {

    private static final SimpleGrantedAuthority AUTHORITY_USER =
            new SimpleGrantedAuthority("USER");

    @Autowired
    private JwtAuthenticationService jwtAuthenticationService;

    @Test(expected = IllegalArgumentException.class)
    public void givenNullPrincipal_whenCreateJwtToken_thenBadCredentialsException() {
        String jwtToken = jwtAuthenticationService.createJwtToken(null, 1);
        jwtAuthenticationService.parseJwtToken(jwtToken);
    }

    @Test(expected = BadCredentialsException.class)
    public void givenInvalidPrincipal_whenCreateJwtToken_thenBadCredentialsException() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "invalid",
                        null,
                        singletonList(AUTHORITY_USER)
                );

        String jwtToken = jwtAuthenticationService.createJwtToken(authentication, 1);
        jwtAuthenticationService.parseJwtToken(jwtToken);
    }

    @Test
    public void givenValidPrincipal_whenCreateJwtToken_thenParsed() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "53245345345345",
                        null,
                        singletonList(AUTHORITY_USER)
                );

        String jwtToken = jwtAuthenticationService.createJwtToken(authentication, 1);

        authentication = jwtAuthenticationService.parseJwtToken(jwtToken);

        assertEquals("53245345345345", authentication.getName());
        assertThat(authentication.getAuthorities(), contains(AUTHORITY_USER));
    }

    @Test
    public void givenValidPrincipal_whenParseJwtToken_thenAuthenticated() {
        Authentication authentication = jwtAuthenticationService.parseJwtToken(JWT_TOKEN_VALID);
        assertEquals("53245345345345", authentication.getName());
        assertThat(authentication.getAuthorities(), contains(AUTHORITY_USER));
        assertTrue(authentication.isAuthenticated());
    }

    @Test(expected = BadCredentialsException.class)
    public void givenInvalidPrincipal_whenParseJwtToken_thenBadCredentialsException() {
        jwtAuthenticationService.parseJwtToken(JWT_TOKEN_INVALID);
    }

    @Test(expected = BadCredentialsException.class)
    public void givenNullPrincipal_whenParseJwtToken_thenBadCredentialsException() {
        jwtAuthenticationService.parseJwtToken(null);
    }

    @Test(expected = NonceExpiredException.class)
    public void givenExpiredPrincipal_whenParseJwtToken_thenNonceExpiredException() {
        jwtAuthenticationService.parseJwtToken(JWT_TOKEN_EXPIRED);
    }

}
