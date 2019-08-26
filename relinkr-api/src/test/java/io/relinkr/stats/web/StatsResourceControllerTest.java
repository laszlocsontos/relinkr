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

package io.relinkr.stats.web;

import static io.relinkr.test.Mocks.FIXED_CLOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.stats.web.StatsResourceControllerTest.TestConfig;
import io.relinkr.test.security.AbstractResourceControllerTest;
import java.time.Clock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
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
        .perform(get("/v1/stats/links/PAST_WEEK"))
        .andExpect(status().isOk())
        .andDo(print());

    // TODO: assert contents
  }

  @TestConfiguration
  @Import({AbstractResourceControllerTest.TestConfig.class})
  public static class TestConfig {

    @Bean
    Clock clock() {
      return FIXED_CLOCK;
    }

  }

}
