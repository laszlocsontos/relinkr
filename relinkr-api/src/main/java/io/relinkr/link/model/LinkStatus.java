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

package io.relinkr.link.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.hateoas.Identifiable;

/**
 * Represents the {@link Link}'s current status.
 */
public enum LinkStatus implements Identifiable<String> {

  // NOTE: Enum values cannot be forward-referenced here

  /**
   * The link is currently active, that is, it performs a normal redirect when accessed.
   */
  ACTIVE(true, "ARCHIVED", "BROKEN"),

  /**
   * Background check revealed that the link is broken, thus it cannot be accessed.
   * TODO: Implement background link checks.
   */
  BROKEN(false, "ACTIVE", "ARCHIVED"),

  /**
   * Means that the links has been just created and it's waiting to be checked.
   * NOTE: Currently {@code ACTIVE} is the initial state of links.
   * TODO: Implement background link checks.
   */
  PENDING(false, "ACTIVE", "BROKEN"),

  /**
   * The link is achived by the user.
   * TODO: Implemented automatic removal of archived links.
   */
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
