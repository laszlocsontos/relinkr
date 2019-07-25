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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.Stats.StatType;
import io.relinkr.stats.model.TimePeriod;
import io.relinkr.stats.model.TimeSpan;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class StatsResourceAssembler implements ResourceAssembler<Stats<?>, StatsResource> {

  @Override
  public StatsResource toResource(Stats<?> stats) {
    StatsResource resource = new StatsResource(stats);

    TimeSpan selfTs = stats.getCurrentTimeSpan();
    Link link = getLinkBuilder(stats.getType(), selfTs.getPeriod()).withSelfRel();
    resource.add(link);

    addAvailableLinks(stats, resource);

    return resource;
  }

  private void addAvailableLinks(Stats<?> stats, StatsResource resource) {
    for (TimeSpan ts : stats.getAvailableTimeSpans()) {
      Link link = getLinkBuilder(stats.getType(), ts.getPeriod()).withRel(ts.getPeriod().name());
      resource.add(link);
    }
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
