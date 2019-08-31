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
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.relinkr.core.model.TimeZone;
import io.relinkr.core.web.EntityResource;
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
import lombok.NonNull;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;
import org.springframework.util.Assert;

/**
 * DTO which represents a {@link User} as a REST resource.
 */
@Getter
@Setter
@Relation(value = "user", collectionRelation = "users")
@SuppressFBWarnings(
    value = {"EQ_DOESNT_OVERRIDE_EQUALS"},
    justification = "Overriding equals() wouldn't contribute to the class' identity"
)
class UserResource extends ResourceSupport {

  @JsonUnwrapped
  private final EntityResource entityResource;

  private final String emailAddress;

  @JsonProperty("userProfile")
  private final UserProfileResource userProfileResource;

  @JsonProperty("userPreferences")
  private final UserPreferencesResource userPreferencesResource;

  private UserResource(
      EntityResource entityResource,
      String emailAddress,
      UserProfileResource userProfileResource,
      UserPreferencesResource userPreferencesResource) {

    this.entityResource = entityResource;
    this.emailAddress = emailAddress;
    this.userProfileResource = userProfileResource;
    this.userPreferencesResource = userPreferencesResource;
  }

  static UserResource of(@NonNull User user, UserProfileType userProfileType) {
    String emailAddress = user.getEmailAddress().getValue();

    UserProfileResource userProfileResource = Optional.ofNullable(userProfileType)
        .flatMap(user::getUserProfile)
        .map(UserProfileResource::of)
        .orElse(null);

    UserPreferencesResource userPreferencesResource =
        UserPreferencesResource.of(user.getUserPreferences());

    UserResource userResource = new UserResource(
        EntityResource.of(user), emailAddress, userProfileResource, userPreferencesResource
    );

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
  static class UserProfileResource {

    private final UserProfileType userProfileType;
    private final String userProfileId;

    private final String fullName;
    private final String givenName;
    private final String middleName;
    private final String familyName;

    private final URI profileUrl;
    private final URI pictureUrl;

    @Enumerated(STRING)
    private final Gender gender;

    private final LocalDate birthDate;

    UserProfileResource(
        UserProfileType userProfileType, String userProfileId,
        String fullName, String givenName, String middleName, String familyName,
        URI profileUrl, URI pictureUrl, Gender gender, LocalDate birthDate) {

      this.userProfileType = userProfileType;
      this.userProfileId = userProfileId;
      this.fullName = fullName;
      this.givenName = givenName;
      this.middleName = middleName;
      this.familyName = familyName;
      this.profileUrl = profileUrl;
      this.pictureUrl = pictureUrl;
      this.gender = gender;
      this.birthDate = birthDate;
    }

    static UserProfileResource of(UserProfile userProfile) {
      UserProfileType userProfileType = userProfile.getUserProfileType();
      Assert.notNull(userProfileType, "userProfileType cannot be null");

      String userProfileId = userProfile.getUserProfileId();
      Assert.notNull(userProfileId, "userProfileId cannot be null");

      return new UserProfileResource(
          userProfileType,
          userProfileId,
          userProfile.getFullName().orElse(null),
          userProfile.getGivenName().orElse(null),
          userProfile.getMiddleName().orElse(null),
          userProfile.getFamilyName().orElse(null),
          userProfile.getProfileUrl().orElse(null),
          userProfile.getPictureUrl().orElse(null),
          userProfile.getGender().orElse(null),
          userProfile.getBirthDate().orElse(null)
      );
    }

  }

  @Data
  static class UserPreferencesResource {

    private final TimeZone timeZone;
    private final Locale locale;

    UserPreferencesResource(TimeZone timeZone, Locale locale) {
      this.timeZone = timeZone;
      this.locale = locale;
    }

    static UserPreferencesResource of(UserPreferences userPreferences) {
      TimeZone timeZone = userPreferences.getTimeZone();
      Locale locale = userPreferences.getLocale();
      return new UserPreferencesResource(timeZone, locale);
    }

  }

}
