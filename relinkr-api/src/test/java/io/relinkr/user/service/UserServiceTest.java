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

package io.relinkr.user.service;

import static io.relinkr.test.Mocks.EMAIL_ADDRESS;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.createUser;
import static io.relinkr.test.Mocks.createUserProfile;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;

import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.user.model.Role;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserProfile;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.OptimisticLockingFailureException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  private UserService userService;

  private User user;
  private UserProfile userProfile;

  @Before
  public void setUp() {
    userService = new UserServiceImpl(userRepository);

    user = createUser();
    userProfile = createUserProfile();

    reset(userRepository);
    given(userRepository.save(any(User.class))).will(i -> i.getArgument(0));
  }

  @Test
  public void givenNonExistentUser_whenSaveUser_thenNewCreated() {
    given(userRepository.findByEmailAddress(EMAIL_ADDRESS)).willReturn(Optional.empty());
    userService.saveUser(EMAIL_ADDRESS, userProfile);
    assertUser(
        2,
        user -> assertNotNull(user.getUserProfile(userProfile.getUserProfileType()))
    );
  }

  @Test
  public void givenExistingUser_whenSaveUser_thenUpdated() {
    given(userRepository.findByEmailAddress(EMAIL_ADDRESS)).willReturn(Optional.of(user));
    userService.saveUser(EMAIL_ADDRESS, userProfile);
    assertUser(
        1,
        user -> assertNotNull(user.getUserProfile(userProfile.getUserProfileType()))
    );
  }

  @Test
  public void givenExistingUserAlreadyUpdated_whenSaveUser_thenUpdated() {
    given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
    given(userRepository.findByEmailAddress(EMAIL_ADDRESS)).willReturn(Optional.of(user));
    given(userRepository.save(any(User.class))).willReturn(user);

    // Simulate one failure upon flush
    doThrow(new OptimisticLockingFailureException("error"))
        .doNothing()
        .when(userRepository)
        .flush();

    userService.saveUser(EMAIL_ADDRESS, userProfile);

    assertUser(
        2,
        user -> assertNotNull(user.getUserProfile(userProfile.getUserProfileType()))
    );
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentUser_whenGetUser_thenEntityNotFoundException() {
    given(userRepository.findById(USER_ID)).willReturn(Optional.empty());
    userService.getUser(USER_ID);
  }

  @Test
  public void givenExistingUnlockedUser_whenLockUser_thenLocked() {
    given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
    userService.lockUser(USER_ID);
    assertUser(1, user -> assertTrue(user.isLocked()));
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentUser_whenLockUser_thenEntityNotFoundException() {
    given(userRepository.findById(USER_ID)).willReturn(Optional.empty());
    userService.lockUser(USER_ID);
  }

  @Test
  public void givenExistingLockedUser_whenUnlockUser_thenUnlocked() {
    given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
    userService.unlockUser(USER_ID);
    assertUser(1, user -> assertFalse(user.isLocked()));
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentUser_whenUnlockUser_thenEntityNotFoundException() {
    given(userRepository.findById(USER_ID)).willReturn(Optional.empty());
    userService.unlockUser(USER_ID);
    assertUser(1, user -> assertFalse(user.isLocked()));
  }

  @Test
  public void givenExistingUserWithoutRole_whenGrantRole_thenGranted() {
    given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
    userService.grantRole(USER_ID, Role.ADMIN);
    assertUser(1, user -> assertThat(user.getRoles(), hasItem(Role.ADMIN)));
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentUser_whenGrantRole_thenEntityNotFoundException() {
    given(userRepository.findById(USER_ID)).willReturn(Optional.empty());
    userService.grantRole(USER_ID, Role.ADMIN);
  }

  @Test
  public void givenExistingUserWithRole_whenRevokeRole_thenRevoked() {
    given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
    userService.grantRole(USER_ID, Role.ADMIN);
    userService.revokeRole(USER_ID, Role.ADMIN);
    assertUser(2, user -> assertThat(user.getRoles(), not(hasItem(Role.ADMIN))));
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentUser_whenRevokeRole_thenEntityNotFoundException() {
    given(userRepository.findById(USER_ID)).willReturn(Optional.empty());
    userService.revokeRole(USER_ID, Role.ADMIN);
  }

  private void assertUser(int invocations, Consumer<User> savedUserAssertor) {
    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    then(userRepository).should(times(invocations)).save(userArgumentCaptor.capture());
    savedUserAssertor.accept(userArgumentCaptor.getValue());
  }

}
