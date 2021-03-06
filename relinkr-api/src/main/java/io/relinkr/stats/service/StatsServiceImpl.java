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
import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.TimeSpan;
import io.relinkr.user.model.UserId;
import java.time.LocalDate;
import java.util.Collection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

  private final StatsRepository<LocalDate> linkStatsRepository;
  private final StatsRepository<LocalDate> clickStatsRepository;
  private final StatsRepository<String> visitorStatsRepository;

  @Override
  public Stats<LocalDate> getLinkStats(@NonNull UserId userId, @NonNull TimeSpan timeSpan) {
    Collection<StatEntry<LocalDate>> entries =
        linkStatsRepository.fetchStats(userId, timeSpan.getStartDate(), timeSpan.getEndDate());

    return Stats.ofLinks(entries, timeSpan);
  }

  @Override
  public Stats<LocalDate> getClickStats(@NonNull UserId userId, @NonNull TimeSpan timeSpan) {
    Collection<StatEntry<LocalDate>> entries =
        clickStatsRepository.fetchStats(userId, timeSpan.getStartDate(), timeSpan.getEndDate());

    return Stats.ofClicks(entries, timeSpan);
  }

  @Override
  public Stats<String> getVisitorStats(@NonNull UserId userId, @NonNull TimeSpan timeSpan) {
    Collection<StatEntry<String>> entries =
        visitorStatsRepository.fetchStats(userId, timeSpan.getStartDate(), timeSpan.getEndDate());

    return Stats.ofVisitors(entries, timeSpan);
  }

}
