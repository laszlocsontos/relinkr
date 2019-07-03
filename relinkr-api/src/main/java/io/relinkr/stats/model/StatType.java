package io.relinkr.stats.model;

import io.relinkr.stats.web.StatsResourceController;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public enum StatType {
    LINKS {
        public ControllerLinkBuilder linkBuilder() {
            return linkTo(methodOn(StatsResourceController.class).getLinksStats(null));
        }
    },
    CLICKS {
        public ControllerLinkBuilder linkBuilder() {
            return linkTo(methodOn(StatsResourceController.class).getClicksStats(null));
        }
    },
    VISITORS {
        public ControllerLinkBuilder linkBuilder() {
            return linkTo(methodOn(StatsResourceController.class).getVisitorsStats(null));
        }
    };

    public abstract ControllerLinkBuilder linkBuilder();
}
