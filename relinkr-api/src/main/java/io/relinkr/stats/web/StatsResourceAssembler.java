package io.relinkr.stats.web;

import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.TimeSpan;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class StatsResourceAssembler <K> implements ResourceAssembler<Stats<K>, StatsResource> {

    @Override
    public StatsResource toResource(Stats<K> stats) {
        StatsResource resource = new StatsResource(stats);

        TimeSpan<K> selfTs = stats.getTimeSpan();
        String queryStr = getQueryString(selfTs);
        Link link = stats.getType().linkBuilder().slash(queryStr).withSelfRel();
        resource.add(link);

        addAvailableLinks(stats, resource);

        return resource;
    }

    private void addAvailableLinks(Stats<K> stats, StatsResource resource) {
        for(TimeSpan<K> ts : stats.getAvailableTimeSpans()) {
            String queryStr = getQueryString(ts);
            Link link = stats.getType().linkBuilder().slash(queryStr).withRel(ts.getName());
            resource.add(link);
        }
    }

    private String getQueryString(TimeSpan<K> ts) {
        return String.format("?startDate=%s&endDate=%s", ts.getStartDate(), ts.getEndDate());
    }
}
