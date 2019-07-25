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
    String queryStr = getQueryString(selfTs);
    Link link = getLinkBuilder(stats.getType()).slash(queryStr).withSelfRel();
    resource.add(link);

    addAvailableLinks(stats, resource);

    return resource;
  }

  private void addAvailableLinks(Stats<?> stats, StatsResource resource) {
    for (TimeSpan ts : stats.getAvailableTimeSpans()) {
      String queryStr = getQueryString(ts);
      Link link = getLinkBuilder(stats.getType()).slash(queryStr).withRel(ts.getName());
      resource.add(link);
    }
  }

  private ControllerLinkBuilder getLinkBuilder(StatType statType) {
    StatsResourceController controller = methodOn(StatsResourceController.class);
    switch (statType) {
      case LINKS:
        return linkTo(controller.getLinksStats(null));
      case CLICKS:
        return linkTo(controller.getClicksStats(null));
      case VISITORS:
        return linkTo(controller.getVisitorsStats(null));
      default:
        throw new AssertionError("Internal error: unknown type " + statType);
    }
  }

  private String getQueryString(TimeSpan ts) {
    return String.format("?startDate=%s&endDate=%s", ts.getStartDate(), ts.getEndDate());
  }

}
