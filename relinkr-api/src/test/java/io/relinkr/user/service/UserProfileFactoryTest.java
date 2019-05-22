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

import static io.relinkr.test.Mocks.GOOGLE_USER_ATTRIBUTES;
import static org.junit.Assert.assertEquals;

import io.relinkr.user.model.UserProfile;
import io.relinkr.user.model.UserProfileType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class UserProfileFactoryTest {

  private final UserProfileFactory userProfileFactory = new UserProfileFactoryImpl();

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void givenGoogleUserInfo_whenCreate_thenOk() {
    UserProfile userProfile = userProfileFactory
        .create(UserProfileType.GOOGLE, GOOGLE_USER_ATTRIBUTES);

    assertEquals(UserProfileType.GOOGLE, userProfile.getUserProfileType());
    assertEquals("12345789", userProfile.getUserProfileId());
    assertEquals("László Csontos", userProfile.getFullName().get());
    assertEquals("László", userProfile.getGivenName().get());
    assertEquals("Csontos", userProfile.getFamilyName().get());
    assertEquals(URI.create("https://plus.google.com/104401221461109262503"),
        userProfile.getProfileUrl().get());
    assertEquals(URI.create(
        "https://lh3.googleusercontent.com/-7EVTpxqEgj8/AAAAAAAAAAI/AAAAAAAAAAA/Qo9wrOAoxPU/photo.jpg"),
        userProfile.getPictureUrl().get());
  }

  @Test
  public void givenFacebookUserInfo_whenCreate_thenOk() {
    Map<String, Object> userAttributes = new HashMap<>();
    userAttributes.put("id", "12345789");
    userAttributes.put("name", "László Csontos");
    userAttributes.put("first_name", "László");
    userAttributes.put("last_name", "Csontos");
    userAttributes.put("link", "https://www.facebook.com/app_scoped_user_id/807050522759429/");
    userAttributes.put("profile_pic",
        "https://lh3.googleusercontent.com/-7EVTpxqEgj8/AAAAAAAAAAI/AAAAAAAAAAA/Qo9wrOAoxPU/photo.jpg");
    userAttributes.put("birthday", "05/06");

    UserProfile userProfile = userProfileFactory
        .create(UserProfileType.FACEBOOK, userAttributes);

    assertEquals(UserProfileType.FACEBOOK, userProfile.getUserProfileType());
    assertEquals("12345789", userProfile.getUserProfileId());
    assertEquals("László Csontos", userProfile.getFullName().get());
    assertEquals("László", userProfile.getGivenName().get());
    assertEquals("Csontos", userProfile.getFamilyName().get());
    assertEquals(URI.create("https://www.facebook.com/app_scoped_user_id/807050522759429/"),
        userProfile.getProfileUrl().get());
    assertEquals(URI.create(
        "https://lh3.googleusercontent.com/-7EVTpxqEgj8/AAAAAAAAAAI/AAAAAAAAAAA/Qo9wrOAoxPU/photo.jpg"),
        userProfile.getPictureUrl().get());
  }

}
