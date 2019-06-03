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

import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserId;
import io.relinkr.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

/**
 * Delegates to {@link UserService} in order to comply with the contact of
 * {@link UserDetailsService}. This component is used by
 * {@link org.springframework.security.web.authentication.www.BasicAuthenticationFilter} thought
 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
 */
@Component
public class DelegatingUserDetailsService implements UserDetailsService {

  private final UserService delegate;

  public DelegatingUserDetailsService(UserService delegate) {
    this.delegate = delegate;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (StringUtils.isEmpty(username)) {
      throw new UsernameNotFoundException("Empty user name");
    }

    long userId = 0;
    try {
      userId = NumberUtils.parseNumber(username, Long.class);
    } catch (IllegalArgumentException iae) {
      throw new UsernameNotFoundException("Invalid user ID", iae);
    }

    try {
      User user = delegate.getUser(UserId.of(userId));
      return DelegatingUserDetails.of(user);
    } catch (EntityNotFoundException enfe) {
      throw new UsernameNotFoundException("User with ID " + username + " doesn't exist");
    }
  }

}
