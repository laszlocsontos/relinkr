package com.springuni.hermes.core.security.authn;

import static com.springuni.hermes.Mocks.EMAIL_ADDRESS;
import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.createUser;
import static com.springuni.hermes.Mocks.createUserProfile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.BASIC;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.springuni.hermes.core.security.authn.WebOAuth2LoginTest.TestController;
import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.Role;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.model.UserProfile;
import com.springuni.hermes.user.model.UserProfileType;
import javax.servlet.http.HttpServletRequest;
import org.assertj.core.util.Maps;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = TestController.class)
public class WebOAuth2LoginTest extends AbstractWebSecurityTest {

    private static final String CLIENT_REG_ID = "google";
    private static final String STATE = "state";
    private static final String REDIRECT_URI = "http://localhost/login/oauth2/code/google";

    @MockBean
    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;

    @MockBean
    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

    @MockBean(name = "defaultOAuth2UserService")
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService;

    private User user;
    private UserProfile userProfile;

    private ResultActions resultActions;

    @Before
    public void setUp() throws Exception {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(CLIENT_REG_ID)
                .authorizationGrantType(AUTHORIZATION_CODE)
                .clientId("1234")
                .clientSecret("1234")
                .clientName("google")
                .clientAuthenticationMethod(BASIC)
                .redirectUriTemplate("/")
                .scope("email")
                .authorizationUri("/")
                .tokenUri("/")
                .build();

        given(clientRegistrationRepository.findByRegistrationId(CLIENT_REG_ID))
                .willReturn(clientRegistration);

        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest
                .authorizationCode()
                .authorizationUri("/")
                .clientId("1234")
                .state(STATE)
                .redirectUri(REDIRECT_URI)
                .additionalParameters(
                        Maps.newHashMap(OAuth2ParameterNames.REGISTRATION_ID, CLIENT_REG_ID))
                .build();

        given(authorizationRequestRepository.loadAuthorizationRequest(
                any(HttpServletRequest.class))).willReturn(authorizationRequest);

        OAuth2AccessTokenResponse accessTokenResponse = OAuth2AccessTokenResponse.withToken("1234")
                .tokenType(BEARER).build();

        given(accessTokenResponseClient.getTokenResponse(
                any(OAuth2AuthorizationCodeGrantRequest.class))).willReturn(accessTokenResponse);

        OAuth2User oAuth2User = new DefaultOAuth2User(
                Sets.newLinkedHashSet(new SimpleGrantedAuthority("ROLE_USER")),
                Maps.newHashMap("email", EMAIL_ADDRESS.getValue()),
                "email"
        );

        given(defaultOAuth2UserService.loadUser(any(OAuth2UserRequest.class)))
                .willReturn(oAuth2User);

        user = createUser();

        given(userService.saveUser(any(EmailAddress.class), any(UserProfile.class)))
                .willReturn(user);

        userProfile = createUserProfile();

        given(userProfileFactory.create(any(UserProfileType.class), anyMap()))
                .willReturn(userProfile);

        resultActions = performLogin();
    }

    @Test
    public void givenSuccessfulOAuth2Login_thenUserSaved() throws Exception {
        then(userService).should().saveUser(EMAIL_ADDRESS, userProfile);
    }

    @Test
    public void givenSuccessfulOAuth2Login_thenAuthenticationNameIsInternalUserId() throws Exception {
        resultActions.andExpect(authenticated().withAuthenticationName(String.valueOf(USER_ID)));
    }

    @Test
    public void givenSuccessfulOAuth2Login_thenAuthoritiesTakenFromInternalUser() throws Exception {
        String[] roles = user.getRoles()
                .stream()
                .map(Role::name)
                .toArray(String[]::new);

        resultActions.andExpect(authenticated().withRoles(roles));
    }

    @Test
    public void givenSuccessfulOAuth2Login_thenRedirectedToDashboard() throws Exception {
        resultActions
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/pages/dashboard"));
    }

    private ResultActions performLogin() throws Exception {
        return mockMvc.perform(
                post("/login/oauth2/code/{regId}", CLIENT_REG_ID)
                        .param("code", "code")
                        .param("state", STATE)
                        .param("redirectUri", REDIRECT_URI))
                .andDo(print());
    }

    @Controller
    public static class TestController {

    }

}
