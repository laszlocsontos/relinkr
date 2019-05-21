package io.relinkr.core.security.authn.oauth2;

import static io.relinkr.core.security.authn.WebSecurityConfig.OAUTH2_LOGIN_PROCESSES_BASE_URI;
import static io.relinkr.test.Mocks.EMAIL_ADDRESS;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.createUser;
import static io.relinkr.test.Mocks.createUserProfile;
import static java.util.Collections.singletonMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.security.config.oauth2.client.CommonOAuth2Provider.GOOGLE;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REGISTRATION_ID;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.core.security.authn.jwt.JwtAuthenticationService;
import io.relinkr.core.security.authn.jwt.JwtAuthenticationTokenCookieResolver;
import io.relinkr.core.security.authn.oauth2.OAuth2LoginTest.TestController;
import io.relinkr.test.security.AbstractWebSecurityTest;
import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.model.Role;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserProfile;
import io.relinkr.user.model.UserProfileType;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

@Slf4j
@WebMvcTest(controllers = TestController.class)
public class OAuth2LoginTest extends AbstractWebSecurityTest {

  private static final String CLIENT_ID = "1234";
  private static final String CLIENT_REG_ID = "google";
  private static final String STATE = "state";

  private static final String BASE_URI = "http://localhost";

  private static final String REDIRECT_URI =
      BASE_URI + OAUTH2_LOGIN_PROCESSES_BASE_URI + "/" + CLIENT_REG_ID;
  private static final String CLIENT_SECRET = "1234";

  @Autowired
  private JwtAuthenticationService jwtAuthenticationService;

  @Autowired
  private JwtAuthenticationTokenCookieResolver jwtAuthenticationTokenCookieResolver;

  @MockBean
  private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;

  @MockBean
  private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

  @MockBean(name = "defaultOAuth2UserService")
  private OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService;

  private User user;
  private String[] roles;
  private UserProfile userProfile;

  @Before
  public void setUp() {
    ClientRegistration clientRegistration = GOOGLE.getBuilder(CLIENT_REG_ID)
        .authorizationGrantType(AUTHORIZATION_CODE)
        .clientId(CLIENT_ID)
        .clientSecret(CLIENT_SECRET)
        .scope("email")
        .build();

    given(clientRegistrationRepository.findByRegistrationId(CLIENT_REG_ID))
        .willReturn(clientRegistration);

    OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest
        .authorizationCode()
        .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
        .clientId(CLIENT_ID)
        .state(STATE)
        .redirectUri(REDIRECT_URI)
        .additionalParameters(singletonMap(REGISTRATION_ID, CLIENT_REG_ID))
        .build();

    given(authorizationRequestRepository.removeAuthorizationRequest(
        any(HttpServletRequest.class), any(HttpServletResponse.class)
    )).willReturn(authorizationRequest);

    OAuth2AccessTokenResponse accessTokenResponse = OAuth2AccessTokenResponse.withToken("1234")
        .tokenType(BEARER).build();

    given(accessTokenResponseClient.getTokenResponse(
        any(OAuth2AuthorizationCodeGrantRequest.class))).willReturn(accessTokenResponse);

    OAuth2User oAuth2User = new DefaultOAuth2User(
        Sets.newLinkedHashSet(new SimpleGrantedAuthority("ROLE_USER")),
        singletonMap("email", EMAIL_ADDRESS.getValue()),
        "email"
    );

    given(defaultOAuth2UserService.loadUser(any(OAuth2UserRequest.class)))
        .willReturn(oAuth2User);

    user = createUser();
    roles = user.getRoles()
        .stream()
        .map(Role::name)
        .toArray(String[]::new);

    given(userService.saveUser(any(EmailAddress.class), any(UserProfile.class)))
        .willReturn(user);

    userProfile = createUserProfile();

    given(userProfileFactory.create(any(UserProfileType.class), anyMap()))
        .willReturn(userProfile);
  }

  @Test
  public void givenSuccessfulCallback_whenLogin_thenJwtSetAndUserSaved()
      throws Exception {

    performLoginWithState(STATE)
        .andExpect(status().isFound())
        .andExpect(redirectedUrlTemplate("https://localhost:9443/login"))
        .andExpect(
            new JwtMatcher(jwtAuthenticationService)
                .withAuthenticationName(String.valueOf(USER_ID))
                .withRoles(roles)
        );

    then(userService).should().saveUser(EMAIL_ADDRESS, userProfile);
  }

  @Test
  public void givenInvalidState_whenLogin_thenUnauthorizedAndUserIsNotSaved()
      throws Exception {

    performLoginWithState("bad")
        .andExpect(status().isFound())
        .andExpect(
            redirectedUrlTemplate(
                "https://localhost:9443/login?error={error}",
                "[invalid_state_parameter] "
            )
        )
        .andExpect(new JwtMatcher(jwtAuthenticationService).withoutAuthentication());

    then(userService).should(never()).saveUser(EMAIL_ADDRESS, userProfile);
  }

  @Test
  public void givenInvalidEmailAddress_whenLogin_thenUnauthorizedAndUserIsNotSaved()
      throws Exception {

    // We expect that spring.security.oauth2.client.provider.<provider_name>.userNameAttribute
    // be always set to email and hence we expect a valid email address from the OAuth2 server.
    OAuth2User oAuth2User = new DefaultOAuth2User(
        Sets.newLinkedHashSet(new SimpleGrantedAuthority("ROLE_USER")),
        singletonMap("email", "bad"),
        "email"
    );

    given(defaultOAuth2UserService.loadUser(any(OAuth2UserRequest.class)))
        .willReturn(oAuth2User);

    performLoginWithState(STATE)
        .andExpect(status().isFound())
        .andExpect(
            redirectedUrlTemplate(
                "https://localhost:9443/login?error={error}",
                "[invalid_email_address] Invalid email address: bad"
            )
        )
        .andExpect(new JwtMatcher(jwtAuthenticationService).withoutAuthentication());

    then(userService).should(never()).saveUser(EMAIL_ADDRESS, userProfile);
  }

  private ResultActions performLoginWithState(String state) throws Exception {
    return mockMvc.perform(
        get(OAUTH2_LOGIN_PROCESSES_BASE_URI + "/{regId}", CLIENT_REG_ID)
            .param("code", "code")
            .param("state", state)
            .param("redirectUri", REDIRECT_URI))
        .andDo(print());
  }

  @Controller
  public static class TestController {

  }

  @RequiredArgsConstructor
  private class JwtMatcher implements ResultMatcher {

    private final JwtAuthenticationService jwtAuthenticationService;

    private boolean expectNoAuthentication = false;

    private String expectedAuthenticationName;
    private Collection<? extends GrantedAuthority> expectedGrantedAuthorities;

    @Override
    public void match(MvcResult result) {
      Authentication auth = load(result);

      if (expectNoAuthentication) {
        assertTrue("Authentication should be null", auth == null);
        return;
      }

      assertTrue("Authentication should not be null", auth != null);

      if (this.expectedAuthenticationName != null) {
        String name = auth.getName();
        assertEquals(
            this.expectedAuthenticationName + " does not equal " + name,
            this.expectedAuthenticationName,
            name
        );
      }

      if (this.expectedGrantedAuthorities != null) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        assertTrue(
            authorities + " does not contain the same authorities as "
                + this.expectedGrantedAuthorities,
            authorities.containsAll(this.expectedGrantedAuthorities)
        );

        assertTrue(
            this.expectedGrantedAuthorities
                + " does not contain the same authorities as " + authorities,
            this.expectedGrantedAuthorities.containsAll(authorities)
        );
      }
    }

    JwtMatcher withoutAuthentication() {
      this.expectNoAuthentication = true;
      this.expectedAuthenticationName = null;
      this.expectedGrantedAuthorities = null;
      return this;
    }

    JwtMatcher withAuthenticationName(String expected) {
      this.expectedAuthenticationName = expected;
      return this;
    }

    JwtMatcher withAuthorities(Collection<? extends GrantedAuthority> expected) {
      this.expectedGrantedAuthorities = expected;
      return this;
    }

    JwtMatcher withRoles(String... roles) {
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      for (String role : roles) {
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
      }
      return withAuthorities(authorities);
    }

    private Authentication load(MvcResult result) {
      MockHttpServletRequest request =
          new MockHttpServletRequest(result.getRequest().getServletContext());

      request.setCookies(result.getResponse().getCookies());

      return jwtAuthenticationTokenCookieResolver.resolveToken(request)
          .map(jwtAuthenticationService::parseJwtToken)
          .orElse(null);
    }

  }

}
