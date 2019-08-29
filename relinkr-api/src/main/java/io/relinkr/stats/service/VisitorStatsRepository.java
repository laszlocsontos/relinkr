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

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

@Component
class VisitorStatsRepository extends AbstractStatsRepository<String> {

  private static final String SQL = "SELECT 'returning' AS key, COUNT(*) AS value FROM ("
      + "SELECT c.visitor_id FROM click c "
      + "WHERE c.visit_timestamp >= :start_date AND c.visit_timestamp <= :end_date "
      + "AND c.user_id = :user_id GROUP BY c.visitor_id HAVING COUNT(*) > 1"
      + ") AS r "
      + "UNION ALL "
      + "SELECT 'new' AS key, COUNT(*) AS value FROM ("
      + "SELECT c.visitor_id FROM click c "
      + "WHERE c.visit_timestamp >= :start_date AND c.visit_timestamp <= :end_date "
      + "AND c.user_id = :user_id GROUP BY c.visitor_id HAVING COUNT(*) = 1"
      + ") AS n";

  private static KeyMapper<String> STRING_KEY_MAPPER = (rs -> rs.getString(1));

  VisitorStatsRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
    super(SQL, STRING_KEY_MAPPER, namedParameterJdbcOperations);
  }

}
