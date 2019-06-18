package io.relinkr.test.security;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.security.test.context.TestSecurityContextHolder.clearContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.After;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
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

  @TestConfiguration
  @EnableHypermediaSupport(type = HAL)
  @EnableSpringDataWebSupport
  @Import({AbstractWebSecurityTest.TestConfig.class})
  public static class TestConfig {

  }

}
