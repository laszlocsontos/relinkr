package io.relinkr.user.service;

import static io.relinkr.test.Mocks.EMAIL_ADDRESS;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.USER_ID_ZERO;
import static io.relinkr.test.Mocks.createUserProfile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import io.relinkr.test.orm.BaseRepositoryTest;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserId;
import io.relinkr.user.model.UserProfile;
import io.relinkr.user.model.UserProfileType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest extends BaseRepositoryTest<User, UserId, UserRepository> {

  private UserProfile userProfile;

  @Override
  public void setUp() throws Exception {
    userProfile = createUserProfile();
    super.setUp();
  }

  @Test
  public void givenSavedUser_whenFindByEmailAddress_thenFound() {
    saveEntity();
    assertEquals(entity.getId(), repository.findByEmailAddress(EMAIL_ADDRESS).get().getId());
  }

  @Test
  public void givenUserWithProfileSaved_whenFindByEmailAddress_thenProfileIsThere() {
    saveEntity();
    UserProfileType userProfileType = userProfile.getUserProfileType();
    entity = repository.findByEmailAddress(EMAIL_ADDRESS).get();
    assertEquals(userProfile, entity.getUserProfile(userProfileType).get());
  }

  @Test
  public void givenSavedUser_whenDeleteById_thenFound() {
    saveEntity();
    repository.deleteById(entity.getId());
    assertFalse(repository.findById(USER_ID).isPresent());
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void givenNonExistentUser_whenDeleteById_thenFound() {
    repository.deleteById(USER_ID);
  }

  @Override
  protected User createEntity() {
    User user = new User(EMAIL_ADDRESS, "Secret");
    user.addUserProfile(userProfile);
    return user;
  }

  @Override
  protected UserId getId() {
    return USER_ID;
  }

  @Override
  protected UserId getNonExistentId() {
    return USER_ID_ZERO;
  }

}
