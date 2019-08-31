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

package io.relinkr.user.model;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PACKAGE;

import java.io.Serializable;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * As {@link User}s are idenfied by their email address, they might have multiple profiles when they
 * get authenticated through an OAuth2 provider or natively with their password.
 */
@Embeddable
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor(staticName = "of")
@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode
@ToString(of = {"userProfileType", "fullName"})
public class UserProfile implements Serializable {

  private static final long serialVersionUID = 3311727408768297384L;

  @NonNull
  @Enumerated(STRING)
  @Column(name = "user_profile_type")
  private UserProfileType userProfileType;

  @NonNull
  private String userProfileId;

  private String fullName;
  private String givenName;
  private String middleName;
  private String familyName;

  private URI profileUrl;
  private URI pictureUrl;

  // FIXME: gender and birthDate are to be removed, the app doesn't need to handle such a
  //  personal detail.

  @Enumerated(STRING)
  private Gender gender;

  private LocalDate birthDate;

  public UserProfileType getUserProfileType() {
    return userProfileType;
  }

  public String getUserProfileId() {
    return userProfileId;
  }

  public Optional<String> getFullName() {
    return Optional.ofNullable(fullName);
  }

  public Optional<String> getGivenName() {
    return Optional.ofNullable(givenName);
  }

  public Optional<String> getMiddleName() {
    return Optional.ofNullable(middleName);
  }

  public Optional<String> getFamilyName() {
    return Optional.ofNullable(familyName);
  }

  public Optional<URI> getProfileUrl() {
    return Optional.ofNullable(profileUrl);
  }

  public Optional<URI> getPictureUrl() {
    return Optional.ofNullable(pictureUrl);
  }

  public Optional<Gender> getGender() {
    return Optional.ofNullable(gender);
  }

  public Optional<LocalDate> getBirthDate() {
    return Optional.ofNullable(birthDate);
  }

}
