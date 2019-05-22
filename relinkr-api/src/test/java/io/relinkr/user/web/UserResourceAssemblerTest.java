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
