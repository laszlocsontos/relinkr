package io.relinkr.test.security;

import static io.relinkr.test.Mocks.FIXED_INSTANT;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.security.core.context.SecurityContextHolder.createEmptyContext;
import static org.springframework.security.test.context.TestSecurityContextHolder.clearContext;
import static org.springframework.security.test.context.TestSecurityContextHolder.setContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import io.relinkr.core.security.authn.user.UserAuthenticationToken;
import io.relinkr.core.security.authz.MethodSecurityConfig;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserProfileType;
import javax.persistence.EntityManager;
import org.junit.After;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.web.servlet.ResultActions;

public abstract class AbstractResourceControllerTest extends AbstractWebSecurityTest {

  @MockBean
  protected EntityManager entityManager;

  @After
  public void tearDown() {
    clearContext();
  }

  protected void assertError(
      int expectedStatusCode, String expectedDetailMessage, ResultActions resultActions)
      throws Exception {

    resultActions
        .andExpect(jsonPath("$.statusCode", is(expectedStatusCode)))
        .andExpect(jsonPath("$.detailMessage", startsWith(expectedDetailMessage)));
  }

  protected void withUser(User user, UserProfileType userProfileType) {
    Authentication authentication = UserAuthenticationToken.of(
        user.getId().getId(),
        userProfileType,
        FIXED_INSTANT.getEpochSecond(),
        createAuthorityList("ROLE_USER")
    );

    SecurityContext context = createEmptyContext();
    context.setAuthentication(authentication);
    setContext(context);
  }

  @TestConfiguration
  @EnableHypermediaSupport(type = HAL)
  @EnableSpringDataWebSupport
  @Import({AbstractWebSecurityTest.TestConfig.class, MethodSecurityConfig.class})
  public static class TestConfig {

  }

}
