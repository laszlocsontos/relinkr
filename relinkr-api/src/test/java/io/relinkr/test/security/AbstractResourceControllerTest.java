package io.relinkr.test.security;

import static io.relinkr.test.Mocks.FIXED_INSTANT;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.security.core.context.SecurityContextHolder.createEmptyContext;
import static org.springframework.security.core.context.SecurityContextHolder.setContext;
import static org.springframework.security.test.context.TestSecurityContextHolder.clearContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import io.relinkr.core.model.EmailAddress;
import io.relinkr.core.security.authn.user.EmailAddressAuthenticationToken;
import org.junit.After;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.web.servlet.ResultActions;

public abstract class AbstractResourceControllerTest extends AbstractWebSecurityTest {

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

  protected void withUser(EmailAddress principal, long expiresAt) {
    Authentication authentication = EmailAddressAuthenticationToken.of(
        principal,
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
  @Import({AbstractWebSecurityTest.TestConfig.class})
  public static class TestConfig {

  }

}
