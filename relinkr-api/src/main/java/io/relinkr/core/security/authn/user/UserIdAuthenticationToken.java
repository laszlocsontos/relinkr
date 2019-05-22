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

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserIdAuthenticationToken extends AbstractAuthenticationToken {

  private final long userId;

  private UserIdAuthenticationToken(
      long userId, Collection<? extends GrantedAuthority> authorities) {

    super(authorities);
    setAuthenticated(true);
    this.userId = userId;
  }

  public static UserIdAuthenticationToken of(long userId,
      Collection<? extends GrantedAuthority> authorities) {
    return new UserIdAuthenticationToken(userId, authorities);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Long getPrincipal() {
    return userId;
  }

}
