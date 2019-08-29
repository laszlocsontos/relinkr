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

import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.TimeSpan;
import io.relinkr.user.model.UserId;
import java.time.LocalDate;

/**
 * Provides the service layer for the statistics module.
 */
public interface StatsService {

  /**
   * Gather time-series statistics for links created by the given {@code userId}.
   *
   * @param userId user's ID owning the links
   * @param timeSpan {@link TimeSpan} for which the statistics should be calculated
   * @return Statistics of link for the given {@code timeSpan}
   */
  Stats<LocalDate> getLinkStats(UserId userId, TimeSpan timeSpan);

  /**
   * Gather time-series statistics for clicks of links owned by the given {@code userId}.
   *
   * @param userId user's ID owning the links
   * @param timeSpan {@link TimeSpan} for which the statistics should be calculated
   * @return Statistics of clicks for the given {@code timeSpan}
   */
  Stats<LocalDate> getClickStats(UserId userId, TimeSpan timeSpan);

  /**
   * Gather new vs. returning statistics for visitors of links owned by the given {@code userId}.
   *
   * @param userId user's ID owning the links
   * @param timeSpan {@link TimeSpan} for which the statistics should be calculated
   * @return Statistics of visitors for the given {@code timeSpan}
   */
  Stats<String> getVisitorStats(UserId userId, TimeSpan timeSpan);

}
