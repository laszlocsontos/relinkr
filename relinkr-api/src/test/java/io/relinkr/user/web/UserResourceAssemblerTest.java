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

package io.relinkr.user.web;

import static io.relinkr.test.Mocks.createUser;
import static io.relinkr.test.Mocks.createUserProfile;
import static io.relinkr.user.model.UserProfileType.GOOGLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import io.relinkr.user.model.User;
import io.relinkr.user.model.UserProfile;
import org.junit.Test;

public class UserResourceAssemblerTest {

  private final UserResourceAssembler userResourceAssembler = new UserResourceAssembler();

  @Test(expected = IllegalArgumentException.class)
  public void givenNullUser_whenToResource_thenIllegalArgumentException() {
    userResourceAssembler.toResource(null, null);
  }

  @Test
  public void givenUserWithoutProfileType_whenToResource_thenResourceCreatedWithoutProfile() {
    User user = createUser();

    UserResource userResource = userResourceAssembler.toResource(user, null);

    assertEquals(String.valueOf(user.getId()), userResource.getResourceId());
    assertEquals(user.getEmailAddress().getValue(), userResource.getEmailAddress());
    assertFalse(userResource.getUserProfile().isPresent());
  }

  @Test
  public void givenUserWithProfileType_whenToResource_thenResourceCreatedWithProfile() {
    User user = createUser();
    user.addUserProfile(createUserProfile());

    UserResource userResource = userResourceAssembler.toResource(user, GOOGLE);

    assertEquals(String.valueOf(user.getId()), userResource.getResourceId());
    assertEquals(user.getEmailAddress().getValue(), userResource.getEmailAddress());

    UserProfile userProfile = user.getUserProfile(GOOGLE).get();
    assertEquals(userProfile, userResource.getUserProfile().get());
  }

}
