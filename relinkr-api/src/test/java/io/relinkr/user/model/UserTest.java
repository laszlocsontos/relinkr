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

import static io.relinkr.test.Mocks.createUser;
import static io.relinkr.test.Mocks.createUserProfile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

  private User user;

  @Before
  public void setUp() {
    user = createUser();
  }

  @Test
  public void givenUnlockedUser_whenLock_thenLocked() {
    user.lock();
    assertTrue(user.isLocked());
  }

  @Test
  public void givenLockedUser_withUnlock_thenUnlocked() {
    user.lock();
    user.unlock();
    assertFalse(user.isLocked());
  }

  @Test
  public void givenNormal_whenGrantRole_thenGranted() {
    user.grantRole(Role.ADMIN);
    assertTrue(user.isAdmin());
  }

  @Test
  public void givenAdminRole_whenRevokeRole_thenRevoked() {
    user.grantRole(Role.ADMIN);
    user.revokeRole(Role.ADMIN);
    assertFalse(user.isAdmin());
  }

  @Test
  public void givenNoUserProfiles_whenAddUserProfile_thenProfileAdded() {
    UserProfile userProfile = createUserProfile();
    user.addUserProfile(userProfile);
    assertEquals(userProfile, user.getUserProfile(userProfile.getUserProfileType()).get());
  }

}
