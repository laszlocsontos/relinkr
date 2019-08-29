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

package io.relinkr.stats.service;

import static io.relinkr.test.Mocks.ENTRIES_BY_DATE;
import static io.relinkr.test.Mocks.ENTRIES_BY_STRING;
import static io.relinkr.test.Mocks.TIME_SPAN;
import static io.relinkr.test.Mocks.USER_ID;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StatsServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private StatsRepository<LocalDate> linkStatsRepository;

  @Mock
  private StatsRepository<LocalDate> clickStatsRepository;

  @Mock
  private StatsRepository<String> visitorStatsRepository;

  private StatsService statsService;

  @Before
  public void setUp() {
    statsService = new StatsServiceImpl(
        linkStatsRepository, clickStatsRepository, visitorStatsRepository
    );
  }

  @Test
  public void givenNullUserId_whenGetLinksStats_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("userId is marked @NonNull but is null");

    statsService.getLinksStats(null, TIME_SPAN);
  }

  @Test
  public void givenNullUserId_whenGetClicksStats_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("userId is marked @NonNull but is null");

    statsService.getClicksStats(null, TIME_SPAN);
  }

  @Test
  public void givenNullUserId_whenGetVisitorsStats_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("userId is marked @NonNull but is null");

    statsService.getVisitorsStats(null, TIME_SPAN);
  }

  @Test
  public void givenNullTimeSpan_whenGetLinksStats_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("timeSpan is marked @NonNull but is null");

    statsService.getLinksStats(USER_ID, null);
  }

  @Test
  public void givenNullTimeSpan_whenGetClicksStats_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("timeSpan is marked @NonNull but is null");

    statsService.getClicksStats(USER_ID, null);
  }

  @Test
  public void givenNullTimeSpan_whenGetVisitorsStats_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("timeSpan is marked @NonNull but is null");

    statsService.getVisitorsStats(USER_ID, null);
  }

  @Test
  public void givenUserIdAndTimeSpan_whenGetLinksStats_thenIllegalArgumentException() {
    given(linkStatsRepository.fetchStats(USER_ID, TIME_SPAN.getStartDate(), TIME_SPAN.getEndDate()))
        .willReturn(ENTRIES_BY_DATE);

    statsService.getLinksStats(USER_ID, TIME_SPAN);

    then(linkStatsRepository)
        .should()
        .fetchStats(USER_ID, TIME_SPAN.getStartDate(), TIME_SPAN.getEndDate());
  }

  @Test
  public void givenUserIdAndTimeSpan_whenGetClicksStats_thenIllegalArgumentException() {
    given(
        clickStatsRepository.fetchStats(USER_ID, TIME_SPAN.getStartDate(), TIME_SPAN.getEndDate())
    ).willReturn(ENTRIES_BY_DATE);

    statsService.getClicksStats(USER_ID, TIME_SPAN);

    then(clickStatsRepository)
        .should()
        .fetchStats(USER_ID, TIME_SPAN.getStartDate(), TIME_SPAN.getEndDate());
  }

  @Test
  public void givenUserIdAndTimeSpan_whenGetVisitorsStats_thenIllegalArgumentException() {
    given(
        visitorStatsRepository.fetchStats(USER_ID, TIME_SPAN.getStartDate(), TIME_SPAN.getEndDate())
    ).willReturn(ENTRIES_BY_STRING);

    statsService.getVisitorsStats(USER_ID, TIME_SPAN);

    then(visitorStatsRepository)
        .should()
        .fetchStats(USER_ID, TIME_SPAN.getStartDate(), TIME_SPAN.getEndDate());
  }

}
