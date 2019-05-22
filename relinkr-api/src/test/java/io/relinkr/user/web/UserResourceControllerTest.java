package io.relinkr.user.web;

import static io.relinkr.test.Mocks.USER_ID_ZERO;
import static io.relinkr.test.Mocks.createUser;
import static io.relinkr.test.Mocks.createUserProfile;
import static javax.persistence.LockModeType.NONE;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.test.security.AbstractResourceControllerTest;
import io.relinkr.user.model.Gender;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserProfile;
import io.relinkr.user.web.UserResourceControllerTest.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = UserResourceController.class)
public class UserResourceControllerTest extends AbstractResourceControllerTest {

  private User user;
  private UserProfile userProfile;

  @Before
  public void setUp() {
    user = createUser();
    userProfile = createUserProfile();
    user.addUserProfile(userProfile);

    given(entityManager.find(User.class, user.getId(), NONE)).willReturn(user);
    given(userService.getUser(user.getId())).willReturn(user);
  }

  @Test
  public void givenOwnUserIdWithAuthenticatedUser_whenGetUser_thenOk() throws Exception {
    withUser(user, userProfile.getUserProfileType());

    ResultActions resultActions = mockMvc
        .perform(get("/api/users/{userId}", user.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(HAL_JSON_VALUE))
        .andDo(print());

    assertUser(resultActions);
  }

  @Test
  public void givenOtherUserIdWithAuthenticatedUser_whenGetUser_thenOk() throws Exception {
    withUser(user, userProfile.getUserProfileType());

    User otherUser = createUser();
    otherUser.setId(USER_ID_ZERO);

    given(entityManager.find(User.class, otherUser.getId(), NONE)).willReturn(otherUser);
    given(userService.getUser(otherUser.getId())).willReturn(otherUser);

    ResultActions resultActions = mockMvc
        .perform(get("/api/users/{userId}", otherUser.getId()))
        .andExpect(status().isForbidden())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andDo(print());
  }

  @Test
  public void givenExistingUserIdWithoutAuthenticatedUser_whenGetUser_thenOk() throws Exception {
    given(userService.getUser(user.getId())).willReturn(user);

    ResultActions resultActions = mockMvc
        .perform(get("/api/users/{userId}", user.getId()))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andDo(print());
  }

  private void assertUser(ResultActions resultActions) throws Exception {
    long userId = user.getId().getId();

    resultActions
        .andExpect(jsonPath("$._links.self.href", is("http://localhost/api/users/" + userId)))
        .andExpect(jsonPath("$.id", is(String.valueOf(userId))))
        .andExpect(jsonPath("$.emailAddress", is(user.getEmailAddress().getValue())))

        .andExpect(
            jsonPath(
                "$.userProfile.userProfileType",
                is(userProfile.getUserProfileType().name())
            )
        )
        .andExpect(jsonPath("$.userProfile.userProfileId", is(userProfile.getUserProfileId())))
        .andExpect(jsonPath("$.userProfile.fullName", is(userProfile.getFullName().orElse(null))))
        .andExpect(jsonPath("$.userProfile.givenName", is(userProfile.getGivenName().orElse(null))))
        .andExpect(
            jsonPath(
                "$.userProfile.middleName",
                is(userProfile.getMiddleName().orElse(null))
            )
        )
        .andExpect(
            jsonPath(
                "$.userProfile.familyName",
                is(userProfile.getFamilyName().orElse(null))
            )
        )
        .andExpect(
            jsonPath(
                "$.userProfile.profileUrl",
                is(userProfile.getProfileUrl().orElse(null))
            )
        )
        .andExpect(
            jsonPath(
                "$.userProfile.pictureUrl",
                is(userProfile.getPictureUrl().orElse(null))
            )
        )
        .andExpect(jsonPath(
            "$.userProfile.gender",
            is(userProfile.getGender().map(Gender::name).orElse(null))
            )
        )
        .andExpect(
            jsonPath(
                "$.userProfile.birthDate",
                is(userProfile.getBirthDate().orElse(null))
            )
        )
        .andExpect(
            jsonPath(
                "$.userPreferences.timeZone",
                is(user.getUserPreferences().getTimeZone().name())
            )
        )
        .andExpect(
            jsonPath(
                "$.userPreferences.locale",
                is(user.getUserPreferences().getLocale().getLanguage())
            )
        );
  }

  @TestConfiguration
  @Import(AbstractResourceControllerTest.TestConfig.class)
  public static class TestConfig {

    @Bean
    UserResourceAssembler userResourceAssembler() {
      return new UserResourceAssembler();
    }

  }

}
