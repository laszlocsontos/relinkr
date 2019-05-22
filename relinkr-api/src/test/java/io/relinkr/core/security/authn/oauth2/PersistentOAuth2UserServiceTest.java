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

import static io.relinkr.core.security.authn.oauth2.PersistentOAuth2UserService.INVALID_EMAIL_ADDRESS;
import static io.relinkr.core.security.authn.oauth2.PersistentOAuth2UserService.INVALID_PROFILE;
import static io.relinkr.core.security.authn.oauth2.PersistentOAuth2UserService.USER_ID_ATTRIBUTE;
import static io.relinkr.test.Mocks.EMAIL_ADDRESS;
import static io.relinkr.test.Mocks.GOOGLE_USER_ATTRIBUTES;
import static io.relinkr.test.Mocks.createUser;
import static io.relinkr.user.model.UserProfileType.GOOGLE;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.SERVER_ERROR;

import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserProfile;
import io.relinkr.user.service.UserProfileFactory;
import io.relinkr.user.service.UserProfileFactoryImpl;
import io.relinkr.user.service.UserService;
import java.util.Map;
import java.util.function.Supplier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RunWith(MockitoJUnitRunner.class)
public class PersistentOAuth2UserServiceTest {

  private final UserProfileFactory userProfileFactory = new UserProfileFactoryImpl();

  @Mock
  private UserService userService;

  private User user;
  private UserProfile userProfile;

  private PersistentOAuth2UserService persistentOAuth2UserService;

  @Before
  public void setUp() throws Exception {
    user = createUser();
    userProfile = userProfileFactory.create(GOOGLE, GOOGLE_USER_ATTRIBUTES);

    persistentOAuth2UserService =
        new PersistentOAuth2UserService(userProfileFactory, userService);
  }

  @Test
  public void givenPrincipalIsNotAnEmailAddress_whenExtractEmailAddress_thenInvalidEmailAddress() {
    OAuth2Error oAuth2Error =
        extractOAuth2Error(() -> persistentOAuth2UserService.extractEmailAddress("bad"));

    assertOAuth2Error(INVALID_EMAIL_ADDRESS, oAuth2Error);
  }

  @Test
  public void givenPrincipalIsAnEmailAddress_whenExtractEmailAddress_thenReturn() {
    EmailAddress emailAddress =
        persistentOAuth2UserService.extractEmailAddress(EMAIL_ADDRESS.getValue());

    assertEquals(EMAIL_ADDRESS, emailAddress);
  }

  @Test
  public void givenUnknownRegistrationId_whenExtractUserProfile_thenInvalidProfile() {
    OAuth2Error oAuth2Error =
        extractOAuth2Error(() -> persistentOAuth2UserService
            .extractUserProfile("unknown", emptyMap()));

    assertOAuth2Error(INVALID_PROFILE, oAuth2Error);
  }

  @Test
  public void givenMissingProfileId_whenExtractUserProfile_thenInvalidProfile() {
    OAuth2Error oAuth2Error =
        extractOAuth2Error(() -> persistentOAuth2UserService.extractUserProfile(
            GOOGLE.name(), emptyMap()));

    assertOAuth2Error(INVALID_PROFILE, oAuth2Error);
  }

  @Test
  public void givenValidProfile_whenExtractUserProfile_thenReturn() {
    UserProfile userProfile =
        persistentOAuth2UserService
            .extractUserProfile(GOOGLE.name(), GOOGLE_USER_ATTRIBUTES);

    assertEquals(this.userProfile, userProfile);
  }

  @Test
  public void givenSaveSuccessful_whenSaveUser_thenReturn() {
    given(userService.saveUser(EMAIL_ADDRESS, userProfile)).willReturn(this.user);
    User user = persistentOAuth2UserService.saveUser(EMAIL_ADDRESS, userProfile);
    assertEquals(this.user, user);
  }

  @Test
  public void givenSaveFailed_whenSaveUser_thenServerError() {
    given(userService.saveUser(EMAIL_ADDRESS, userProfile))
        .willThrow(new QueryTimeoutException("query timed out"));

    OAuth2Error oAuth2Error =
        extractOAuth2Error(
            () -> persistentOAuth2UserService.saveUser(EMAIL_ADDRESS, userProfile));

    assertOAuth2Error(SERVER_ERROR, oAuth2Error);
  }

  @Test
  public void givenOriginalAttributes_whenCreateOAuth2User_thenCustomAttributesAdded() {
    OAuth2User oAuth2User = persistentOAuth2UserService
        .createOAuth2User(GOOGLE_USER_ATTRIBUTES, user, userProfile);

    Map<String, Object> userAttributes = oAuth2User.getAttributes();
    assertThat(userAttributes, hasKey(USER_ID_ATTRIBUTE));
    assertThat(userAttributes, hasValue(user.getId().getId()));

    assertThat(userAttributes, hasKey(USER_ID_ATTRIBUTE));
    assertThat(userAttributes, hasValue(GOOGLE));

    assertThat(oAuth2User.getAuthorities(),
        containsInAnyOrder(
            new SimpleGrantedAuthority("ROLE_USER"),
            new SimpleGrantedAuthority("ROLE_ADMIN")
        )
    );
  }

  private void assertOAuth2Error(String expectedErrorCode, OAuth2Error oAuth2Error) {
    assertNotNull(oAuth2Error);
    assertEquals(expectedErrorCode, oAuth2Error.getErrorCode());
  }

  private OAuth2Error extractOAuth2Error(Supplier<?> callUserTest) {
    try {
      callUserTest.get();
      return null;
    } catch (OAuth2AuthenticationException e) {
      return e.getError();
    }
  }

}
