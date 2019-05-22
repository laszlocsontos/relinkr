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

import static java.util.stream.Collectors.toList;

import io.relinkr.user.model.User;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by lcsontos on 5/24/17.
 */
public class DelegatingUserDetails implements UserDetails {

  private final User delegate;

  public DelegatingUserDetails(User delegate) {
    this.delegate = delegate;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return delegate.getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(toList());
  }

  @Override
  public String getPassword() {
    return delegate.getEncryptedPassword().orElse(null);
  }

  @Override
  public String getUsername() {
    return String.valueOf(delegate.getId());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !delegate.isLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return delegate.isConfirmed();
  }

}
