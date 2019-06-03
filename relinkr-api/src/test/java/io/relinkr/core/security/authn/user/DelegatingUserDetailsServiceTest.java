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

import static io.relinkr.test.Mocks.USER_ID_ZERO;
import static io.relinkr.test.Mocks.createUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserId;
import io.relinkr.user.service.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingUserDetailsServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private final User user = createUser();

  @Mock
  private UserService userService;

  private UserDetailsService userDetailsService;

  @Before
  public void setUp() {
    userDetailsService = new DelegatingUserDetailsService(userService);
  }

  @Test
  public void givenExistingUserId_whenLoadUserByUsername_thenLoaded() {
    UserId userId = user.getId();
    given(userService.getUser(userId)).willReturn(user);

    UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(user.getId()));

    assertEquals(String.valueOf(user.getId()), userDetails.getUsername());
    then(userService).should().getUser(userId);
  }

  @Test
  public void givenEmptyUsername_whenLoadUserByUsername_thenUsernameNotFoundException() {
    expectedException.expect(UsernameNotFoundException.class);
    expectedException.expectMessage("Empty user name");

    try {
      userDetailsService.loadUserByUsername("");
    } finally {
      then(userService).should(never()).getUser(any(UserId.class));
    }
  }

  @Test
  public void givenNonNumericUsername_whenLoadUserByUsername_thenUsernameNotFoundException() {
    expectedException.expect(UsernameNotFoundException.class);
    expectedException.expectMessage("Invalid user ID");

    try {
      userDetailsService.loadUserByUsername("bad");
    } finally {
      then(userService).should(never()).getUser(any(UserId.class));
    }
  }

  @Test
  public void givenExistingUserId_whenLoadUserByUsername_thenUsernameNotFoundException() {
    expectedException.expect(UsernameNotFoundException.class);
    expectedException.expectMessage("User with ID 0 doesn't exist");

    given(userService.getUser(USER_ID_ZERO))
        .willThrow(new EntityNotFoundException("id", USER_ID_ZERO));

    try {
      userDetailsService.loadUserByUsername(String.valueOf(USER_ID_ZERO));
    } finally {
      then(userService).should().getUser(USER_ID_ZERO);
    }
  }

}
