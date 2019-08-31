/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package io.relinkr.core.security.authn.jwt;

import static io.relinkr.core.security.authn.oauth2.PersistentOAuth2UserService.USER_ID_ATTRIBUTE;
import static io.relinkr.core.security.authn.oauth2.PersistentOAuth2UserService.USER_PROFILE_TYPE_ATTRIBUTE;
import static io.relinkr.test.Mocks.AUTHORITY_USER;
import static io.relinkr.test.Mocks.FIXED_CLOCK;
import static io.relinkr.test.Mocks.FIXED_INSTANT;
import static io.relinkr.test.Mocks.JWT_TOKEN_EXPIRED;
import static io.relinkr.test.Mocks.JWT_TOKEN_INVALID;
import static io.relinkr.test.Mocks.JWT_TOKEN_VALID;
import static io.relinkr.user.model.UserProfileType.GOOGLE;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import io.relinkr.core.security.authn.jwt.JwtAuthenticationServiceTest.TestConfig;
import io.relinkr.core.security.authn.user.UserAuthenticationToken.Details;
import io.relinkr.core.util.IdGenerator;
import io.relinkr.core.util.IdentityGenerator;
import io.relinkr.core.util.RandomGenerator;
import java.time.Clock;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
public class JwtAuthenticationServiceTest {

  @Autowired
  private JwtAuthenticationService jwtAuthenticationService;

  @Test(expected = IllegalArgumentException.class)
  public void givenNullAuthentication_whenCreateJwtToken_thenIllegalArgumentException() {
    String jwtToken = jwtAuthenticationService.createJwtToken(null, 1);
    jwtAuthenticationService.parseJwtToken(jwtToken);
  }

  @Test(expected = BadCredentialsException.class)
  public void givenInvalidAuthentication_whenCreateJwtToken_thenBadCredentialsException() {
    Authentication authentication = createAuthentication("invalid");

    String jwtToken = jwtAuthenticationService.createJwtToken(authentication, 1);
    jwtAuthenticationService.parseJwtToken(jwtToken);
  }

  @Test
  public void givenValidAuthentication_whenCreateJwtToken_thenParsed() {
    Authentication authentication = createAuthentication("53245345345345");

    String jwtToken = jwtAuthenticationService.createJwtToken(authentication, 1);

    authentication = jwtAuthenticationService.parseJwtToken(jwtToken);

    assertEquals("53245345345345", authentication.getName());

    assertEquals(
        FIXED_INSTANT.getEpochSecond() + 60,
        ((Details) authentication.getDetails()).getExpiresAt()
    );

    assertThat(authentication.getAuthorities(), contains(AUTHORITY_USER));
  }

  @Test
  public void givenValidOauth2Authentication_whenCreateJwtToken_thenParsed() {
    Authentication authentication = createOauth2Authentication(53245345345345L, GOOGLE);

    String jwtToken = jwtAuthenticationService.createJwtToken(authentication, 1);

    authentication = jwtAuthenticationService.parseJwtToken(jwtToken);

    assertEquals("53245345345345", authentication.getName());

    assertEquals(
        FIXED_INSTANT.getEpochSecond() + 60,
        ((Details) authentication.getDetails()).getExpiresAt()
    );

    assertThat(authentication.getAuthorities(), contains(AUTHORITY_USER));
    assertEquals(GOOGLE, ((Details) authentication.getDetails()).getUserProfileType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenInvalidOauth2Authentication_whenCreateJwtToken_thenIllegalArgumentException() {
    Authentication authentication = createOauth2Authentication(53245345345345L, "invalid");

    String jwtToken = jwtAuthenticationService.createJwtToken(authentication, 1);
    jwtAuthenticationService.parseJwtToken(jwtToken);
  }

  @Test
  public void givenValidToken_whenParseJwtToken_thenAuthenticated() {
    Authentication authentication = jwtAuthenticationService.parseJwtToken(JWT_TOKEN_VALID);

    assertEquals("53245345345345", authentication.getName());
    assertEquals(130407537511L, ((Details) authentication.getDetails()).getExpiresAt());
    assertThat(authentication.getAuthorities(), contains(AUTHORITY_USER));
    assertTrue(authentication.isAuthenticated());
  }

  @Test(expected = BadCredentialsException.class)
  public void givenInvalidToken_whenParseJwtToken_thenBadCredentialsException() {
    jwtAuthenticationService.parseJwtToken(JWT_TOKEN_INVALID);
  }

  @Test(expected = BadCredentialsException.class)
  public void givenNullToken_whenParseJwtToken_thenBadCredentialsException() {
    jwtAuthenticationService.parseJwtToken(null);
  }

  @Test(expected = NonceExpiredException.class)
  public void givenExpiredToken_whenParseJwtToken_thenNonceExpiredException() {
    jwtAuthenticationService.parseJwtToken(JWT_TOKEN_EXPIRED);
  }

  private Authentication createAuthentication(Object principal) {
    return new UsernamePasswordAuthenticationToken(
        principal,
        null,
        singletonList(AUTHORITY_USER)
    );
  }

  private Authentication createOauth2Authentication(Object principal, Object profileType) {
    Map<String, Object> userAttributes = new HashMap<>();
    userAttributes.put(USER_ID_ATTRIBUTE, principal);
    userAttributes.put(USER_PROFILE_TYPE_ATTRIBUTE, profileType);

    Collection<? extends GrantedAuthority> authorities = singletonList(AUTHORITY_USER);

    return new OAuth2AuthenticationToken(
        new DefaultOAuth2User(authorities, userAttributes, USER_ID_ATTRIBUTE),
        authorities,
        CommonOAuth2Provider.GOOGLE.name()
    );
  }

  @Configuration
  @Import(JwtConfig.class)
  static class TestConfig {

    @Bean
    Clock clock() {
      return FIXED_CLOCK;
    }

    @Bean
    RandomGenerator randomGenerator() {
      return new RandomGenerator();
    }

    @Bean
    IdGenerator idGenerator(RandomGenerator randomGenerator) {
      return new IdentityGenerator(randomGenerator);
    }

  }

}
