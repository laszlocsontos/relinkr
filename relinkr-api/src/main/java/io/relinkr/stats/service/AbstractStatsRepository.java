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

import static java.time.format.DateTimeFormatter.ISO_DATE;

import io.relinkr.stats.model.StatEntry;
import io.relinkr.user.model.UserId;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@Slf4j
@RequiredArgsConstructor
class AbstractStatsRepository<K> implements StatsRepository<K> {

  private final String sql;
  private final KeyMapper<K> keyMapper;
  private final NamedParameterJdbcOperations namedParameterJdbcOperations;

  @Override
  public final Collection<StatEntry<K>> fetchStats(
      UserId userId, LocalDate startDate, LocalDate endDate) {

    SqlParameterSource sqlParameterSource = createSqlParameterSource(userId, startDate, endDate);

    RowMapper<StatEntry<K>> rowMapper = (rs, rowNum) -> {
      K key = keyMapper.mapRow(rs);
      Integer value = rs.getObject(2, Integer.class);
      return StatEntry.of(key, value);
    };

    try {
      return namedParameterJdbcOperations.query(sql, sqlParameterSource, rowMapper);
    } catch (DataAccessException dae) {
      Throwable cause = dae.getMostSpecificCause();

      log.error(
          "Error executing SQL {}, parameters: {}, {}, {}; reason: {}.",
          sql, userId, ISO_DATE.format(startDate), ISO_DATE.format(endDate), cause.getMessage(),
          cause
      );

      throw dae;
    }
  }

  private SqlParameterSource createSqlParameterSource(
      UserId userId, LocalDate startDate, LocalDate endDate) {

    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();

    sqlParameterSource.addValue("user_id", userId.getId());
    sqlParameterSource.addValue("start_date", toTimestamp(startDate));
    sqlParameterSource.addValue("end_date", toTimestamp(endDate));

    return sqlParameterSource;
  }

  private Timestamp toTimestamp(LocalDate localDate) {
    return Optional.of(localDate)
        .map(LocalDate::atStartOfDay)
        .map(Timestamp::valueOf)
        .get();
  }

}
