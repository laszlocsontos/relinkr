package io.relinkr.stats.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.test.security.AbstractResourceControllerTest;
import io.relinkr.test.security.AbstractResourceControllerTest.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = StatsResourceController.class)
public class StatsResourceControllerTest extends AbstractResourceControllerTest {

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenAuthenticatedUser_whenGetLinksStats_thenOk() throws Exception {

    ResultActions resultActions = mockMvc
        .perform(get("/v1/stats/links"))
        .andExpect(status().isOk())
        .andDo(print());

    // TODO: assert contents
  }

}
