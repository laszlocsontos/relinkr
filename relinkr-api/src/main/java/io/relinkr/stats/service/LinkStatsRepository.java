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
import org.springframework.stereotype.Repository;

@Repository
class LinkStatsRepository extends AbstractDateKeyedStatsRepository {

  private static final String SQL = "SELECT l.created_date AS key, count(*) AS value FROM ("
      + "SELECT CAST(l.created_date AS DATE) AS created_date, l.id FROM link l "
      + "WHERE l.created_date >= :start_date AND l.created_date <= :end_date "
      + "AND l.user_id = :user_id"
      + ") AS l "
      + "GROUP BY l.created_date";

  LinkStatsRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
    super(SQL, namedParameterJdbcOperations);
  }

}
