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

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

import io.relinkr.core.orm.AbstractEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.Table;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "user_")
public class User extends AbstractEntity<UserId> implements Ownable {

  public static final String ROLE_PREFIX = "ROLE_";

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "email_address"))
  private EmailAddress emailAddress;

  private String encryptedPassword;

  @ElementCollection(fetch = EAGER)
  @Enumerated(STRING)
  @Column(name = "role")
  private Set<Role> roles;

  private boolean confirmed;
  private boolean locked;

  @ElementCollection
  @CollectionTable(name = "user_profile")
  @MapKeyColumn(name = "user_profile_type", insertable = false, updatable = false)
  @MapKeyEnumerated(STRING)
  private Map<UserProfileType, UserProfile> userProfiles;

  @Embedded
  private UserPreferences userPreferences;

  /**
   * Creates a new user with a single {@link Role} {@code USER} and
   * with a default {@link UserPreferences}.
   */
  public User() {
    roles = new LinkedHashSet<>();
    roles.add(Role.USER);

    userProfiles = new LinkedHashMap<>();
    userPreferences = new UserPreferences();
  }

  /**
   * Creates a new user with the given email address and password.
   *
   * @param emailAddress email address
   * @param encryptedPassword password
   */
  public User(EmailAddress emailAddress, String encryptedPassword) {
    this();
    this.emailAddress = emailAddress;
    this.encryptedPassword = encryptedPassword;
  }

  public static User of(EmailAddress emailAddress) {
    return new User(emailAddress, null);
  }

  @Override
  public UserId getUserId() {
    return getId();
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public void confirm() {
    confirmed = true;
  }

  public void lock() {
    locked = true;
  }

  public void unlock() {
    locked = false;
  }

  public boolean isLocked() {
    return locked;
  }

  /**
   * Returns {@code true} if this {@code User} has got an {@link Role#ADMIN} role.
   *
   * @return {@code true} if this {@code User} has got an {@link Role#ADMIN} role
   */
  public boolean isAdmin() {
    if (CollectionUtils.isEmpty(roles)) {
      return false;
    }
    return roles.contains(Role.ADMIN);
  }

  /**
   * Returns the list this user's authorities prefixed with {@code ROLE_}.
   *
   * @return list this user's authorities prefixed with {@code ROLE_}.
   */
  public Set<String> getAuthorities() {
    return getRoles().stream()
        .map(Role::name)
        .map(ROLE_PREFIX::concat)
        .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
  }

  public Set<Role> getRoles() {
    return unmodifiableSet(roles);
  }

  public void grantRole(Role role) {
    roles.add(role);
  }

  public void revokeRole(Role role) {
    roles.remove(role);
  }

  public EmailAddress getEmailAddress() {
    return emailAddress;
  }

  public Optional<String> getEncryptedPassword() {
    return Optional.ofNullable(encryptedPassword);
  }

  public void addUserProfile(UserProfile userProfile) {
    userProfiles.put(userProfile.getUserProfileType(), userProfile);
  }

  public Optional<UserProfile> getUserProfile(UserProfileType userProfileType) {
    return Optional.ofNullable(userProfiles.get(userProfileType));
  }

  public Map<UserProfileType, UserProfile> getUserProfiles() {
    return unmodifiableMap(userProfiles);
  }

  public UserPreferences getUserPreferences() {
    return userPreferences;
  }

  public void setUserPreferences(UserPreferences userPreferences) {
    Assert.notNull(userPreferences, "userPreferences cannot be null");
    this.userPreferences = userPreferences;
  }

}
