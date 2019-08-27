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
import java.util.Collection;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public class VisitorStatsRepositoryTest extends AbstractStatsRepositoryTest<String> {

  @Test
  public void givenMatchingUserIdOverlappingDateRange_whenFetchStats_thenResultReturned() {
    Collection<StatEntry<String>> entries = statsRepository.fetchStats(
        USER_ID, START_DATE, END_DATE
    );

    assertThat(
        entries,
        containsInAnyOrder(
            StatEntry.of("returning", 6),
            StatEntry.of("new", 1)
        )
    );
  }

  @Override
  void assertEmptyResult(Collection<StatEntry<String>> entries) {
    assertThat(
        entries,
        containsInAnyOrder(
            StatEntry.of("returning", 0),
            StatEntry.of("new", 0)
        )
    );
  }

  @Override
  StatsRepository<String> createStatsRepository(
      NamedParameterJdbcOperations namedParameterJdbcOperations) {

    return new VisitorStatsRepository(namedParameterJdbcOperations);
  }

}
