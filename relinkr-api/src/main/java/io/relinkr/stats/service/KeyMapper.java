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

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
interface KeyMapper<K> {

  /**
   * Implementations must implement this method to map each row's key  in the ResultSet. This method
   * should not call {@code next()} on the ResultSet; it is only supposed to map key values of the
   * current row.
   *
   * @param rs the ResultSet to map (pre-initialized for the current row)
   * @return the result object for the current row (may be {@code null})
   * @throws SQLException if a SQLException is encountered getting column values
   */
  K mapRow(ResultSet rs) throws SQLException;

}
