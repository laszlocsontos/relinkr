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
