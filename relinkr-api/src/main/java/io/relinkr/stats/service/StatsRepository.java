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

import io.relinkr.stats.model.StatEntry;
import io.relinkr.user.model.UserId;
import java.time.LocalDate;
import java.util.Collection;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
interface StatsRepository<K> {

  Collection<StatEntry<K>> fetchStats(UserId userId, LocalDate startDate, LocalDate endDate);

}
