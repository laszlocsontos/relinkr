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

import static io.relinkr.stats.model.TimePeriod.CUSTOM;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.security.authn.annotation.CurrentUser;
import io.relinkr.core.security.authz.annotation.AuthorizeRolesOrOwner;
import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.Stats.StatType;
import io.relinkr.stats.model.TimePeriod;
import io.relinkr.stats.model.TimeSpan;
import io.relinkr.stats.service.StatsService;
import io.relinkr.user.model.UserId;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.function.BiFunction;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
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

  @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
  @GetMapping(path = "/links/{period}", produces = HAL_JSON_VALUE)
  HttpEntity<StatsResource> getLinksStats(
      @CurrentUser UserId userId, @PathVariable TimePeriod period)
      throws ApplicationException {

    return generateResponse(userId, period, statsService::getLinkStats);
  }

  @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
  @GetMapping(path = "/clicks/{period}", produces = HAL_JSON_VALUE)
  HttpEntity<StatsResource> getClicksStats(
      @CurrentUser UserId userId, @PathVariable TimePeriod period)
      throws ApplicationException {

    return generateResponse(userId, period, statsService::getClickStats);
  }

  @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
  @GetMapping(path = "/visitors/{period}", produces = HAL_JSON_VALUE)
  HttpEntity<StatsResource> getVisitorsStats(
      @CurrentUser UserId userId, @PathVariable TimePeriod period)
      throws ApplicationException {

    return generateResponse(userId, period, statsService::getVisitorStats);
  }

  private <K> HttpEntity<StatsResource> generateResponse(
      UserId userId, TimePeriod period, BiFunction<UserId, TimeSpan, Stats<K>> statsGetter) {

    TimeSpan timeSpan = toTimeSpan(period);
    Stats<K> stats = statsGetter.apply(userId, timeSpan);
    return ok(statsAssembler.toResource(stats));
  }

  private TimeSpan toTimeSpan(TimePeriod period) {
    LocalDate today = LocalDate.now(clock);
    return period.toTimeSpan(today);
  }

  private static class StatsResourceAssembler
      implements ResourceAssembler<Stats<?>, StatsResource> {

    @Override
    public StatsResource toResource(Stats<?> stats) {
      StatsResource resource = new StatsResource(stats);

      TimeSpan timeSpan = stats.getCurrentTimeSpan();
      TimePeriod period = timeSpan.getPeriod();

      Link selfLink = getLinkBuilder(stats.getType(), period).withSelfRel();
      resource.add(selfLink);

      addAvailableLinks(stats.getType(), period, resource);

      return resource;
    }

    private void addAvailableLinks(
        StatType statType, TimePeriod currentPeriod, StatsResource resource) {

      Arrays.stream(TimePeriod.values())
          .filter(it -> !CUSTOM.equals(it) && !it.equals(currentPeriod))
          .map(it -> getLinkBuilder(statType, it).withRel(it.name()))
          .forEach(resource::add);
    }

    private ControllerLinkBuilder getLinkBuilder(StatType statType, TimePeriod period) {
      StatsResourceController controller = methodOn(StatsResourceController.class);
      switch (statType) {
        case LINKS:
          return linkTo(controller.getLinksStats(null, period));
        case CLICKS:
          return linkTo(controller.getClicksStats(null, period));
        case VISITORS:
          return linkTo(controller.getVisitorsStats(null, period));
        default:
          throw new AssertionError("Internal error: unknown type " + statType);
      }
    }

  }

}
