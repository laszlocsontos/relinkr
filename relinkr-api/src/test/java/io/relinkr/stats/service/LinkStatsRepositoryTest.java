package io.relinkr.stats.service;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import io.relinkr.stats.model.StatEntry;
import java.time.LocalDate;
import java.util.Collection;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public class LinkStatsRepositoryTest extends AbstractStatsRepositoryTest<LocalDate> {

  @Test
  public void givenMatchingUserIdOverlappingDateRange_whenFetchStats_thenResultReturned() {
    Collection<StatEntry<LocalDate>> entries = statsRepository.fetchStats(
        USER_ID, START_DATE, END_DATE
    );

    assertThat(
        entries,
        containsInAnyOrder(
            StatEntry.of(LocalDate.parse("2019-06-19"), 2),
            StatEntry.of(LocalDate.parse("2019-06-20"), 2),
            StatEntry.of(LocalDate.parse("2019-06-21"), 2),
            StatEntry.of(LocalDate.parse("2019-06-22"), 1)
        )
    );
  }

  @Override
  StatsRepository<LocalDate> createStatsRepository(
      NamedParameterJdbcOperations namedParameterJdbcOperations) {

    return new LinkStatsRepository(namedParameterJdbcOperations);
  }

}
