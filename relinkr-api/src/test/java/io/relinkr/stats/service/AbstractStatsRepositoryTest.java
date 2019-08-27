package io.relinkr.stats.service;

import static io.relinkr.test.Mocks.USER_ID_ZERO;
import static org.junit.Assert.assertTrue;

import io.relinkr.core.orm.JdbcConfig;
import io.relinkr.stats.model.StatEntry;
import io.relinkr.stats.service.AbstractStatsRepositoryTest.TestConfig;
import io.relinkr.user.model.UserId;
import java.time.LocalDate;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@JdbcTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Sql({"/stats-test-data.sql"})
@ContextConfiguration(classes = TestConfig.class)
abstract class AbstractStatsRepositoryTest<K> {

  static final UserId USER_ID = UserId.of(158955547337177L);
  static final LocalDate START_DATE = LocalDate.of(2019, 6, 1);
  static final LocalDate END_DATE = LocalDate.of(2019, 6, 30);

  @Autowired
  private NamedParameterJdbcOperations namedParameterJdbcOperations;

  StatsRepository<K> statsRepository;

  @Before
  public void setUp() {
    statsRepository = createStatsRepository(namedParameterJdbcOperations);
  }

  @Test
  public void givenMatchingUserIdAndEarlierDateRange_whenFetchStats_thenEmptyResult() {
    Collection<StatEntry<K>> entries = statsRepository.fetchStats(
        USER_ID, START_DATE.minusMonths(1), END_DATE.minusMonths(1)
    );

    assertEmptyResult(entries);
  }

  @Test
  public void givenMatchingUserIdAndLaterDateRange_whenFetchStats_thenEmptyResult() {
    Collection<StatEntry<K>> entries = statsRepository.fetchStats(
        USER_ID, START_DATE.plusMonths(1), END_DATE.plusMonths(1)
    );

    assertEmptyResult(entries);
  }

  @Test
  public void givenNoMatchingUserId_whenFetchStats_thenEmptyResult() {
    Collection<StatEntry<K>> entries = statsRepository.fetchStats(
        USER_ID_ZERO, START_DATE, END_DATE
    );

    assertEmptyResult(entries);
  }

  void assertEmptyResult(Collection<StatEntry<K>> entries) {
    assertTrue(entries.isEmpty());
  }

  abstract StatsRepository<K> createStatsRepository(
      NamedParameterJdbcOperations namedParameterJdbcOperations
  );

  @Configuration
  @Import(JdbcConfig.class)
  static class TestConfig {

  }

}
