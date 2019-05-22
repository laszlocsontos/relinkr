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

import static io.relinkr.test.Mocks.createUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.model.User;
import io.relinkr.user.service.UserService;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingUserDetailsServiceTest {

  @Mock
  private UserService userService;

  private User user;
  private UserDetailsService userDetailsService;

  @Before
  public void setUp() {
    user = createUser();
    userDetailsService = new DelegatingUserDetailsService(userService);
  }

  @Test
  public void loadUserByUsername() {
    EmailAddress emailAddress = user.getEmailAddress();
    given(userService.findUser(emailAddress)).willReturn(Optional.of(user));

    UserDetails userDetails = userDetailsService.loadUserByUsername(emailAddress.getValue());

    assertEquals(String.valueOf(user.getId()), userDetails.getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void loadUserByUsername_withEmptyUserName() {
    userDetailsService.loadUserByUsername("");
  }

  @Test(expected = UsernameNotFoundException.class)
  public void loadUserByUsername_withNonNumericUserName() {
    userDetailsService.loadUserByUsername("bad");
  }

  @Test(expected = UsernameNotFoundException.class)
  public void loadUserByUsername_withNonExistentUser() {
    EmailAddress emailAddress = user.getEmailAddress();
    given(userService.findUser(emailAddress)).willReturn(Optional.empty());
    userDetailsService.loadUserByUsername(emailAddress.getValue());
  }

}
