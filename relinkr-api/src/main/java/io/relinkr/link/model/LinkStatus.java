package io.relinkr.link.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.hateoas.Identifiable;

public enum LinkStatus implements Identifiable<String> {

  // Enum values cannot be forward-referenced here

  ACTIVE(true, "ARCHIVED", "BROKEN"),
  BROKEN(false, "ACTIVE", "ARCHIVED"),
  PENDING(false, "ACTIVE", "BROKEN"),
  ARCHIVED(true, "ACTIVE");

  private final boolean userSettable;
  private final Set<String> nextLinkStatuses;

  LinkStatus(boolean userSettable, String... nextLinkStatuses) {
    this.userSettable = userSettable;
    this.nextLinkStatuses = Arrays.stream(nextLinkStatuses).collect(Collectors.toSet());
  }

  @Override
  public String getId() {
    return name();
  }

  /**
   * Returns next possible transitions from this {@code LinkStatus}.
   *
   * @return Returns next possible transitions from this {@code LinkStatus}
   */
  public Set<LinkStatus> getNextLinkStatuses() {
    return Collections.unmodifiableSet(
        nextLinkStatuses.stream().map(LinkStatus::valueOf).collect(Collectors.toSet())
    );
  }

  public boolean isUserSettable() {
    return userSettable;
  }

}
