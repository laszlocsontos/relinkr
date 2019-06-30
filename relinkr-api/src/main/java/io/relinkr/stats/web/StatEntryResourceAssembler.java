package io.relinkr.stats.web;

import io.relinkr.stats.model.StatEntry;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class StatEntryResourceAssembler implements ResourceAssembler<StatEntry, Resource<StatEntry>> {

    @Override
    public Resource<StatEntry> toResource(StatEntry statEntry) {
        return new Resource<>(statEntry);
    }
}
