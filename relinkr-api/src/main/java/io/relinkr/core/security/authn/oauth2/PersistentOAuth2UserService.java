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

import static java.util.stream.Collectors.toSet;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.SERVER_ERROR;

import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.model.Role;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserProfile;
import io.relinkr.user.service.UserProfileFactory;
import io.relinkr.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Delegates obtaining the user attributes of the End-User (Resource Owner) from the UserInfo
 * Endpoint to {@link org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService}.
 * From those attributes a {@link UserProfile} is created and along with the {@link User} saved to
 * the database.
 */
@Slf4j
public class PersistentOAuth2UserService
    implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  public static final String USER_ID_ATTRIBUTE = "_user_id";
  public static final String USER_PROFILE_TYPE_ATTRIBUTE = "_user_profile_type";

  static final String INVALID_EMAIL_ADDRESS = "invalid_email_address";
  static final String INVALID_PROFILE = "invalid_profile";

  private final OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultUserService;
  private final UserProfileFactory userProfileFactory;
  private final UserService userService;

  @Autowired
  public PersistentOAuth2UserService(
      OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultUserService,
      UserProfileFactory userProfileFactory,
      UserService userService) {

    this.defaultUserService = defaultUserService;
    this.userProfileFactory = userProfileFactory;
    this.userService = userService;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oauth2User = defaultUserService.loadUser(userRequest);

    // We expect that spring.security.oauth2.client.provider.<provider_name>.userNameAttribute
    // be always set to email and hence we expect a valid email address from the OAuth2 server.
    EmailAddress emailAddress = extractEmailAddress(oauth2User.getName());

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    UserProfile userProfile = extractUserProfile(registrationId, oauth2User.getAttributes());

    User user = saveUser(emailAddress, userProfile);

    return createOAuth2User(oauth2User.getAttributes(), user, userProfile);
  }

  private EmailAddress extractEmailAddress(String principalName) throws OAuth2AuthenticationException {
    try {
      return EmailAddress.of(principalName);
    } catch (IllegalArgumentException iae) {
      throw createOAuth2Error(INVALID_EMAIL_ADDRESS, iae);
    }
  }

  private UserProfile extractUserProfile(String registrationId, Map<String, Object> userAttributes)
      throws OAuth2AuthenticationException {

    try {
      return userProfileFactory.create(registrationId.toUpperCase(), userAttributes);
    } catch (RuntimeException re) {
      throw createOAuth2Error(INVALID_PROFILE, re);
    }
  }

  private User saveUser(EmailAddress emailAddress, UserProfile userProfile)
      throws OAuth2AuthenticationException {

    try {
      return userService.saveUser(emailAddress, userProfile);
    } catch (RuntimeException re) {
      throw createOAuth2Error(SERVER_ERROR, re);
    }
  }

  private OAuth2User createOAuth2User(
      Map<String, Object> originalAttributes, User user, UserProfile userProfile) {

    Set<GrantedAuthority> authorities = user.getRoles().stream()
        .map(Role::name)
        .map("ROLE_"::concat)
        .map(SimpleGrantedAuthority::new)
        .collect(toSet());

    Map<String, Object> userAttributes = new HashMap<>(originalAttributes);

    userAttributes.put(USER_ID_ATTRIBUTE, user.getId().getId());
    userAttributes.put(USER_PROFILE_TYPE_ATTRIBUTE, userProfile.getUserProfileType());

    return new DefaultOAuth2User(authorities, userAttributes, USER_ID_ATTRIBUTE);

  }

  private RuntimeException createOAuth2Error(String code, Throwable cause) {
    OAuth2Error oauth2Error = new OAuth2Error(code, cause.getMessage(), null);

    OAuth2AuthenticationException oauth2Authenticationexception =
        new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), cause);

    throw oauth2Authenticationexception;
  }

}
