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

package io.relinkr.user.web;

import static javax.persistence.EnumType.STRING;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.relinkr.core.model.TimeZone;
import io.relinkr.core.web.AbstractResource;
import io.relinkr.user.model.Gender;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserPreferences;
import io.relinkr.user.model.UserProfile;
import io.relinkr.user.model.UserProfileType;
import java.net.URI;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import javax.persistence.Enumerated;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.hateoas.core.Relation;
import org.springframework.util.Assert;

/**
 * DTO which represents a {@link User} as a REST resource.
 */
@Getter
@Setter
@NoArgsConstructor
@Relation(value = "user", collectionRelation = "users")
class UserResource extends AbstractResource {

  private String emailAddress;

  @JsonProperty("userProfile")
  private UserProfileResource userProfileResource;

  @JsonProperty("userPreferences")
  private UserPreferencesResource userPreferencesResource;

  private UserResource(@NonNull User user, UserProfileType userProfileType) {
    super(user);

    this.emailAddress = user.getEmailAddress().getValue();

    userProfileResource = Optional.ofNullable(userProfileType)
        .flatMap(user::getUserProfile)
        .map(UserProfileResource::new)
        .orElse(null);

    userPreferencesResource = new UserPreferencesResource(user.getUserPreferences());
  }

  static UserResource of(@NonNull User user, UserProfileType userProfileType) {
    UserResource userResource = new UserResource(user, userProfileType);
    userResource.add(linkTo(UserResourceController.class).slash(user.getId()).withSelfRel());
    return userResource;
  }

  @JsonIgnore
  Optional<UserProfile> getUserProfile() {
    return Optional.ofNullable(userProfileResource)
        .map(it -> UserProfile.of(
            it.userProfileType,
            it.userProfileId,
            it.fullName,
            it.givenName,
            it.middleName,
            it.familyName,
            it.profileUrl,
            it.pictureUrl,
            it.gender,
            it.birthDate
        ));
  }

  @JsonIgnore
  Optional<UserPreferences> getUserPreferences() {
    return Optional.ofNullable(userPreferencesResource)
        .map(it -> UserPreferences.of(it.timeZone, it.locale));
  }

  @Data
  @NoArgsConstructor
  static class UserProfileResource {

    private UserProfileType userProfileType;
    private String userProfileId;

    private String fullName;
    private String givenName;
    private String middleName;
    private String familyName;

    private URI profileUrl;
    private URI pictureUrl;

    @Enumerated(STRING)
    private Gender gender;

    private LocalDate birthDate;

    UserProfileResource(UserProfile userProfile) {
      userProfileType = userProfile.getUserProfileType();
      Assert.notNull(userProfileType, "userProfileType cannot be null");

      userProfileId = userProfile.getUserProfileId();
      Assert.notNull(userProfileId, "userProfileId cannot be null");

      userProfile.getFullName().ifPresent(this::setFullName);
      userProfile.getGivenName().ifPresent(this::setGivenName);
      userProfile.getMiddleName().ifPresent(this::setMiddleName);
      userProfile.getFamilyName().ifPresent(this::setFamilyName);

      userProfile.getProfileUrl().ifPresent(this::setProfileUrl);
      userProfile.getPictureUrl().ifPresent(this::setPictureUrl);

      userProfile.getGender().ifPresent(this::setGender);
      userProfile.getBirthDate().ifPresent(this::setBirthDate);
    }

  }

  @Data
  @NoArgsConstructor
  static class UserPreferencesResource {

    private TimeZone timeZone;
    private Locale locale;

    UserPreferencesResource(UserPreferences userPreferences) {
      timeZone = userPreferences.getTimeZone();
      locale = userPreferences.getLocale();
    }

  }

}
