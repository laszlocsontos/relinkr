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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import io.relinkr.stats.model.StatEntry;
import java.time.LocalDate;
import java.util.Collection;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public class ClickStatsRepositoryTest extends AbstractStatsRepositoryTest<LocalDate> {

  @Test
  public void givenMatchingUserIdOverlappingDateRange_whenFetchStats_thenResultReturned() {
    Collection<StatEntry<LocalDate>> entries = statsRepository.fetchStats(
        USER_ID, START_DATE, END_DATE
    );

    assertThat(
        entries,
        containsInAnyOrder(
            StatEntry.of(LocalDate.parse("2019-06-19"), 3),
            StatEntry.of(LocalDate.parse("2019-06-20"), 6),
            StatEntry.of(LocalDate.parse("2019-06-21"), 4),
            StatEntry.of(LocalDate.parse("2019-06-22"), 2)
        )
    );
  }

  @Override
  StatsRepository<LocalDate> createStatsRepository(
      NamedParameterJdbcOperations namedParameterJdbcOperations) {

    return new ClickStatsRepository(namedParameterJdbcOperations);
  }

}
