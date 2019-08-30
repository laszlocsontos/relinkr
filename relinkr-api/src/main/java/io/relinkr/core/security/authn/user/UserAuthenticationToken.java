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

package io.relinkr.core.security.authn.user;

import io.relinkr.user.model.UserProfileType;
import java.util.Collection;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Represents an authenticated {@link io.relinkr.user.model.User} by their
 * {@link io.relinkr.user.model.UserId} and {@link UserProfileType}.
 */
public class UserAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = -3963973665552485151L;

  private final long userId;
  private final Details details;

  private UserAuthenticationToken(
      long userId, UserProfileType userProfileType, long expiresAt,
      Collection<? extends GrantedAuthority> authorities) {

    super(authorities);
    setAuthenticated(true);

    this.userId = userId;
    this.details = Details.of(userProfileType, expiresAt);
  }

  /**
   * Creates a new {@code UserAuthenticationToken} based on the following data.
   *
   * @param userId User's ID
   * @param userProfileType User's profile type
   * @param expiresAt Seconds since the Epoch
   * @param authorities User's authorities
   * @return a {@code UserAuthenticationToken} instance
   */
  public static UserAuthenticationToken of(
      long userId, @NonNull UserProfileType userProfileType, long expiresAt,
      Collection<? extends GrantedAuthority> authorities) {
    return new UserAuthenticationToken(userId, userProfileType, expiresAt, authorities);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Long getPrincipal() {
    return userId;
  }

  @Override
  public Details getDetails() {
    return details;
  }

  @Override
  public void setDetails(Object details) {
    throw new UnsupportedOperationException();
  }

  @Getter
  @RequiredArgsConstructor(staticName = "of")
  public static class Details {

    private final UserProfileType userProfileType;
    private final long expiresAt;

  }

}
