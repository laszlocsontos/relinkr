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

import static io.relinkr.test.Mocks.ENTRIES_BY_DATE;
import static io.relinkr.test.Mocks.ENTRIES_BY_STRING;
import static io.relinkr.test.Mocks.FIXED_CLOCK;
import static io.relinkr.test.Mocks.TIME_SPAN;
import static io.relinkr.test.Mocks.USER_ID;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.stats.model.StatEntry;
import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.TimeSpan;
import io.relinkr.stats.service.StatsService;
import io.relinkr.stats.web.StatsResourceControllerTest.TestConfig;
import io.relinkr.test.security.AbstractResourceControllerTest;
import java.time.Clock;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
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

  private static final Stats<LocalDate> LINK_STATS =
      Stats.ofLinks(ENTRIES_BY_DATE, TIME_SPAN, emptyList());

  private static final Stats<LocalDate> CLICK_STATS =
      Stats.ofClicks(ENTRIES_BY_DATE, TIME_SPAN, emptyList());

  private static final Stats<String> VISITOR_STATS =
      Stats.ofVisitors(ENTRIES_BY_STRING, TIME_SPAN, emptyList());

  @MockBean
  private StatsService statsService;

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenAuthenticatedUser_whenGetLinksStats_thenOk() throws Exception {
    given(statsService.getLinksStats(eq(USER_ID), eq(TIME_SPAN))).willReturn(LINK_STATS);

    ResultActions resultActions = mockMvc
        .perform(get("/v1/stats/links/" + TIME_SPAN.getPeriod().name()))
        .andExpect(status().isOk())
        .andDo(print());

    then(statsService).should().getLinksStats(eq(USER_ID), eq(TIME_SPAN));

    assertStats(LINK_STATS, resultActions);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenAuthenticatedUser_whenClickStats_thenOk() throws Exception {
    given(statsService.getClicksStats(eq(USER_ID), eq(TIME_SPAN))).willReturn(CLICK_STATS);

    ResultActions resultActions = mockMvc
        .perform(get("/v1/stats/clicks/" + TIME_SPAN.getPeriod().name()))
        .andExpect(status().isOk())
        .andDo(print());

    then(statsService).should().getClicksStats(eq(USER_ID), eq(TIME_SPAN));

    assertStats(CLICK_STATS, resultActions);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenAuthenticatedUser_whenVisitorStats_thenOk() throws Exception {
    given(statsService.getVisitorsStats(eq(USER_ID), eq(TIME_SPAN))).willReturn(VISITOR_STATS);

    ResultActions resultActions = mockMvc
        .perform(get("/v1/stats/visitors/" + TIME_SPAN.getPeriod().name()))
        .andExpect(status().isOk())
        .andDo(print());

    then(statsService).should().getVisitorsStats(eq(USER_ID), eq(TIME_SPAN));

    assertStats(VISITOR_STATS, resultActions);
  }

  private void assertStats(Stats<?> expectedStats, ResultActions actions) throws Exception {
    int index = 0;
    for (StatEntry<?> entry : expectedStats.getEntries()) {
      Object key = entry.getKey();
      if (key instanceof LocalDate) {
        key = ISO_DATE.format((LocalDate) key);
      }

      actions.andExpect(jsonPath("$._embedded.data[" + index + "].key", is(key)));
      actions.andExpect(jsonPath("$._embedded.data[" + index + "].value", is(entry.getValue())));
      index++;
    }

    TimeSpan timeSpan = expectedStats.getCurrentTimeSpan();
    String periodAsString = timeSpan.getPeriod().name();

    actions
        .andExpect(
            jsonPath("$._links.self.href",
                is("http://localhost/v1/stats/" + expectedStats.getType().name().toLowerCase()
                    + "/" + periodAsString
                )
            )
        )
        .andExpect(jsonPath("$.timespan.period", is(periodAsString)))
        .andExpect(jsonPath("$.timespan.startDate", is(ISO_DATE.format(timeSpan.getStartDate()))))
        .andExpect(jsonPath("$.timespan.endDate", is(ISO_DATE.format(timeSpan.getEndDate()))));
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
