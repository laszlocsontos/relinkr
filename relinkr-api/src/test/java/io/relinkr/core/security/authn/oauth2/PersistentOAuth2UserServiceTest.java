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

package io.relinkr.core.security.authn.oauth2;

import static io.relinkr.core.security.authn.oauth2.PersistentOAuth2UserService.USER_ID_ATTRIBUTE;
import static io.relinkr.core.security.authn.oauth2.PersistentOAuth2UserService.USER_PROFILE_TYPE_ATTRIBUTE;
import static io.relinkr.test.Mocks.AUTHORITY_USER;
import static io.relinkr.test.Mocks.OAUTH2_CLIENT_REGISTRATION;
import static io.relinkr.test.Mocks.OAUTH2_USER_REQUEST_REQUEST;
import static io.relinkr.test.Mocks.createUser;
import static io.relinkr.test.Mocks.createUserProfile;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserProfile;
import io.relinkr.user.service.UserProfileFactory;
import io.relinkr.user.service.UserService;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RunWith(MockitoJUnitRunner.class)
public class PersistentOAuth2UserServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOauth2UserService;

  @Mock
  private UserProfileFactory userProfileFactory;

  @Mock
  private UserService userService;

  private User user;
  private UserProfile userProfile;

  private PersistentOAuth2UserService persistentOAuth2UserService;

  @Before
  public void setUp() {
    user = createUser();
    userProfile = createUserProfile();

    persistentOAuth2UserService =
        new PersistentOAuth2UserService(defaultOauth2UserService, userProfileFactory, userService);
  }

  @Test
  public void givenRequestWithInvalidEmailAddress_whenLoadUser_thenInvalidMailAddressError() {
    expectedException.expect(OAuth2AuthenticationException.class);
    expectedException.expectMessage("[invalid_email_address] Invalid email address: bad");

    OAuth2User oAuth2User = createOAuth2User("bad");
    given(defaultOauth2UserService.loadUser(OAUTH2_USER_REQUEST_REQUEST)).willReturn(oAuth2User);

    persistentOAuth2UserService.loadUser(OAUTH2_USER_REQUEST_REQUEST);

    then(userService).should(never()).saveUser(any(EmailAddress.class), any(UserProfile.class));
  }

  @Test
  public void givenRequestWithValidEmailAddress_whenLoadUser_thenUserSaved() {
    OAuth2User oAuth2User = createOAuth2User(user.getEmailAddress().getValue());

    given(defaultOauth2UserService.loadUser(OAUTH2_USER_REQUEST_REQUEST)).willReturn(oAuth2User);
    given(userService.saveUser(eq(user.getEmailAddress()), eq(userProfile))).willReturn(user);

    given(
        userProfileFactory.create(
            OAUTH2_CLIENT_REGISTRATION.getRegistrationId().toUpperCase(),
            oAuth2User.getAttributes()
        )
    ).willReturn(userProfile);

    OAuth2User savedAuth2User = persistentOAuth2UserService.loadUser(OAUTH2_USER_REQUEST_REQUEST);
    assertOauth2User(savedAuth2User);

    then(userService).should().saveUser(user.getEmailAddress(), userProfile);
  }

  @Test
  public void givenRequestWithUnknownRegistrationId_whenLoadUser_thenInvalidProfileError() {
    expectedException.expect(OAuth2AuthenticationException.class);
    expectedException.expectMessage("[invalid_profile] ");

    OAuth2User oAuth2User = createOAuth2User(user.getEmailAddress().getValue());

    given(defaultOauth2UserService.loadUser(OAUTH2_USER_REQUEST_REQUEST)).willReturn(oAuth2User);

    given(
        userProfileFactory.create(
            OAUTH2_CLIENT_REGISTRATION.getRegistrationId().toUpperCase(),
            oAuth2User.getAttributes()
        )
    ).willThrow(IllegalArgumentException.class);

    try {
      persistentOAuth2UserService.loadUser(OAUTH2_USER_REQUEST_REQUEST);
    } catch (OAuth2AuthenticationException oae) {
      throw oae;
    } finally {
      then(userService).should(never()).saveUser(user.getEmailAddress(), userProfile);
    }
  }

  @Test
  public void givenUserSaveFailed_whenLoadUser_thenServerError() {
    expectedException.expect(OAuth2AuthenticationException.class);
    expectedException.expectMessage("[server_error] ");

    OAuth2User oAuth2User = createOAuth2User(user.getEmailAddress().getValue());

    given(defaultOauth2UserService.loadUser(OAUTH2_USER_REQUEST_REQUEST)).willReturn(oAuth2User);

    given(userService.saveUser(eq(user.getEmailAddress()), eq(userProfile)))
        .willThrow(new QueryTimeoutException("query timed out"));

    given(
        userProfileFactory.create(
            OAUTH2_CLIENT_REGISTRATION.getRegistrationId().toUpperCase(),
            oAuth2User.getAttributes()
        )
    ).willReturn(userProfile);

    try {
      persistentOAuth2UserService.loadUser(OAUTH2_USER_REQUEST_REQUEST);
    } catch (OAuth2AuthenticationException oae) {
      throw oae;
    }
  }

  private void assertOauth2User(OAuth2User oAuth2User) {
    Map<String, Object> userAttributes = oAuth2User.getAttributes();

    assertThat(
        userAttributes,
        hasEntry(USER_ID_ATTRIBUTE, user.getId().getId())
    );

    assertThat(
        userAttributes,
        hasEntry(USER_PROFILE_TYPE_ATTRIBUTE, userProfile.getUserProfileType())
    );

    assertThat(oAuth2User.getAuthorities(),
        containsInAnyOrder(
            new SimpleGrantedAuthority("ROLE_USER"),
            new SimpleGrantedAuthority("ROLE_ADMIN")
        )
    );
  }

  private OAuth2User createOAuth2User(String email) {
    return new DefaultOAuth2User(
        singletonList(AUTHORITY_USER),
        singletonMap("email", email),
        "email"
    );
  }

}
