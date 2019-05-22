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
import lombok.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserIdAuthenticationToken extends AbstractAuthenticationToken {

  private final long userId;
  private final UserProfileType userProfileType;

  private UserIdAuthenticationToken(
      long userId, UserProfileType userProfileType,
      Collection<? extends GrantedAuthority> authorities) {

    super(authorities);
    setAuthenticated(true);

    this.userId = userId;
    this.userProfileType = userProfileType;
  }

  public static UserIdAuthenticationToken of(
      long userId, @NonNull UserProfileType userProfileType,
      Collection<? extends GrantedAuthority> authorities) {
    return new UserIdAuthenticationToken(userId, userProfileType, authorities);
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
  public UserProfileType getDetails() {
    return userProfileType;
  }

  @Override
  public void setDetails(Object details) {
    throw new UnsupportedOperationException();
  }

}
