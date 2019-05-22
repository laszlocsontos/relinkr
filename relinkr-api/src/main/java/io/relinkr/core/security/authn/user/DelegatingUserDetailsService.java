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

import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by lcsontos on 5/24/17.
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

    EmailAddress emailAddress;
    try {
      emailAddress = EmailAddress.of(username);
    } catch (IllegalArgumentException iae) {
      throw new UsernameNotFoundException("Invalid email address", iae);
    }

    return delegate.findUser(emailAddress)
        .map(DelegatingUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }

}
