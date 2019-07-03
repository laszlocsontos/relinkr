package io.relinkr.stats.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.relinkr.stats.model.StatEntry;
import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.TimeSpan;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

@Getter
public class StatsResource extends Resources<StatEntry> {

    @JsonProperty("timespan")
    private TimeSpan timeSpan;

    public StatsResource(Stats stats, Link... links) {
        super(stats.getEntries(), links);
        timeSpan = stats.getTimeSpan();
    }

}
