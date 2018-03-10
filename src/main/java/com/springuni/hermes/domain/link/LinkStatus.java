package com.springuni.hermes.domain.link;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.hateoas.Identifiable;

public enum LinkStatus implements Identifiable<String> {

    // Enum values cannot be forward-referenced here

    ACTIVE("ARCHIVED", "BROKEN"),
    BROKEN("ACTIVE", "ARCHIVED"),
    PENDING("ACTIVE", "BROKEN"),
    ARCHIVED("ACTIVE");

    private final Set<String> nextLinkStatuses;

    LinkStatus(String... nextLinkStatuses) {
        this.nextLinkStatuses = Arrays.stream(nextLinkStatuses).collect(Collectors.toSet());
    }

    @Override
    public String getId() {
        return name();
    }

    public Set<LinkStatus> getNextLinkStatuses() {
        return Collections.unmodifiableSet(
                nextLinkStatuses.stream().map(LinkStatus::valueOf).collect(Collectors.toSet())
        );
    }

}
