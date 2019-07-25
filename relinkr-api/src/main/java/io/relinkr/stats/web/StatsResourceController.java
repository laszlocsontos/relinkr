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
import io.relinkr.stats.model.TimeSpan;
import io.relinkr.user.model.UserId;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Provides the REST API for retrieving statistics.
 */
@RestController
@RequestMapping("/v1/stats")
public class StatsResourceController {

  private final StatsResourceAssembler statsAssembler = new StatsResourceAssembler();

  @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
  @GetMapping(path = "/links", produces = HAL_JSON_VALUE)
  // TODO: Add path variable: requested timespan
  HttpEntity<StatsResource> getLinksStats(@CurrentUser UserId userId)
      throws ApplicationException {

    List<StatEntry<LocalDate>> entries = Arrays.asList(
        StatEntry.of(LocalDate.of(2018, 3, 6), 5),
        StatEntry.of(LocalDate.of(2018, 3, 7), 2)
    );

    List<TimeSpan> timeSpans = Arrays.asList(
        TimeSpan.of("today", LocalDate.of(2019, 6, 3), LocalDate.of(2019, 6, 3)),
        TimeSpan.of("yesterday", LocalDate.of(2019, 6, 2), LocalDate.of(2019, 6, 2)),
        TimeSpan.of("last7days", LocalDate.of(2019, 5, 27), LocalDate.of(2019, 6, 2))
    );

    TimeSpan ts = TimeSpan.of("custom", LocalDate.of(2019, 3, 6), LocalDate.of(2019, 3, 12));
    Stats<LocalDate> stats = Stats.ofLinks(entries, ts, timeSpans);

    StatsResource resource = statsAssembler.toResource(stats);
    return ok(resource);
  }

  @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
  @GetMapping(path = "/clicks", produces = HAL_JSON_VALUE)
  HttpEntity<StatsResource> getClicksStats(@CurrentUser UserId userId)
      throws ApplicationException {

    throw new NotImplementedException();
  }

  @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
  @GetMapping(path = "/visitors", produces = HAL_JSON_VALUE)
  HttpEntity<StatsResource> getVisitorsStats(@CurrentUser UserId userId)
      throws ApplicationException {

    throw new NotImplementedException();
  }

}
