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

import io.relinkr.click.service.ClickRepository;
import io.relinkr.link.service.LinkRepository;
import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.TimeSpan;
import io.relinkr.user.model.UserId;
import io.relinkr.visitor.service.VisitorRepository;
import java.time.LocalDate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

  private final LinkRepository linkRepository;
  private final ClickRepository clickRepository;
  private final VisitorRepository visitorRepository;

  @Override
  public Stats<LocalDate> getLinksStats(@NonNull UserId userId, @NonNull TimeSpan timeSpan) {
    return null;
  }

  @Override
  public Stats<LocalDate> getClicksStats(@NonNull UserId userId, @NonNull TimeSpan timeSpan) {
    return null;
  }

  @Override
  public Stats<String> getVisitorsStats(@NonNull UserId userId, @NonNull TimeSpan timeSpan) {
    return null;
  }

}
