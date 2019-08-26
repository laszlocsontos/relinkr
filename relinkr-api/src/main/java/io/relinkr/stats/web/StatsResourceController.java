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

package io.relinkr.stats.web;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.security.authn.annotation.CurrentUser;
import io.relinkr.core.security.authz.annotation.AuthorizeRolesOrOwner;
import io.relinkr.stats.model.StatEntry;
import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.TimePeriod;
import io.relinkr.stats.model.TimeSpan;
import io.relinkr.stats.service.StatsService;
import io.relinkr.user.model.UserId;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides the REST API for retrieving statistics.
 */
@RestController
@RequestMapping("/v1/stats")
public class StatsResourceController {

  private final StatsResourceAssembler statsAssembler = new StatsResourceAssembler();
  private final StatsService statsService;
  private final Clock clock;

  @Autowired
  public StatsResourceController(
      StatsService statsService, ObjectProvider<Clock> clockProvider) {

    this.statsService = statsService;
    this.clock = clockProvider.getIfAvailable(Clock::systemUTC);
  }

  // TODO: Add path variable: requested timespan
  @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
  @GetMapping(path = "/links/{period}", produces = HAL_JSON_VALUE)
  HttpEntity<StatsResource> getLinksStats(
      @CurrentUser UserId userId, @PathVariable TimePeriod period)
      throws ApplicationException {

    List<StatEntry<LocalDate>> entries = Arrays.asList(
        StatEntry.of(LocalDate.of(2018, 3, 6), 5),
        StatEntry.of(LocalDate.of(2018, 3, 7), 2)
    );

    List<TimeSpan> availableTimeSpans = Collections.emptyList();

    TimeSpan currentTimeSpan = toTimeSpan(period);

    Stats<LocalDate> stats = Stats.ofLinks(entries, currentTimeSpan, availableTimeSpans);

    StatsResource resource = statsAssembler.toResource(stats);
    return ok(resource);
  }

  @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
  @GetMapping(path = "/clicks/{period}", produces = HAL_JSON_VALUE)
  HttpEntity<StatsResource> getClicksStats(
      @CurrentUser UserId userId, @PathVariable TimePeriod period)
      throws ApplicationException {

    TimeSpan timeSpan = toTimeSpan(period);
    Stats<LocalDate> clickStats = statsService.getClicksStats(userId, timeSpan);
    return ok(statsAssembler.toResource(clickStats));
  }

  @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
  @GetMapping(path = "/visitors/{period}", produces = HAL_JSON_VALUE)
  HttpEntity<StatsResource> getVisitorsStats(
      @CurrentUser UserId userId, @PathVariable TimePeriod period)
      throws ApplicationException {

    TimeSpan timeSpan = toTimeSpan(period);
    Stats<LocalDate> clickStats = statsService.getClicksStats(userId, timeSpan);
    return ok(statsAssembler.toResource(clickStats));
  }

  private TimeSpan toTimeSpan(TimePeriod period) {
    LocalDate today = LocalDate.now(clock);
    return period.toTimeSpan(today);
  }

}
