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

import static java.util.Collections.emptySet;

import io.relinkr.core.orm.AbstractEntity;
import io.relinkr.core.orm.AbstractId;
import io.relinkr.user.model.Ownable;
import io.relinkr.user.model.UserId;
import java.net.URI;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import lombok.NonNull;

@MappedSuperclass
public abstract class LinkBase<ID extends AbstractId<? extends LinkBase<ID>>>
    extends AbstractEntity<ID> implements Ownable {

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "user_id"))
  private UserId userId;

  LinkBase(@NonNull UserId userId) {
    this.userId = userId;
  }

  /*
   * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
   */
  LinkBase() {
  }

  @Override
  public UserId getUserId() {
    return userId;
  }

  public abstract URI getLongUrl();

  public abstract void updateLongUrl(@NonNull String longUrl) throws InvalidUrlException;

  public abstract Set<Tag> getTags();

  public abstract void addTag(Tag tag);

  public abstract void removeTag(Tag tag);

  public abstract LinkStatus getLinkStatus();

  protected abstract void setLinkStatus(LinkStatus linkStatus);

  private void setLinkStatus(LinkStatus linkStatus, Set<LinkStatus> expectedLinkStatuses)
      throws InvalidLinkStatusException {

    expectedLinkStatuses = getLinkStatus().getNextLinkStatuses();
    if (!expectedLinkStatuses.contains(linkStatus)) {
      throw InvalidLinkStatusException.forLinkStatus(linkStatus, expectedLinkStatuses);
    }

    setLinkStatus(linkStatus);
  }

  /**
   * Gets next possible statuses.
   *
   * @return Next possible statuses
   */
  public Set<LinkStatus> getUserLinkStatuses() {
    // Users cannot change state in PENDING state
    if (LinkStatus.PENDING.equals(getLinkStatus())) {
      return emptySet();
    }

    return Collections.unmodifiableSet(
        getLinkStatus().getNextLinkStatuses().stream().filter(LinkStatus::isUserSettable)
            .collect(Collectors.toSet())
    );
  }

  public void markActive() throws InvalidLinkStatusException {
    setLinkStatus(LinkStatus.ACTIVE, getLinkStatus().getNextLinkStatuses());
  }

  public void markArchived() throws InvalidLinkStatusException {
    setLinkStatus(LinkStatus.ARCHIVED, getLinkStatus().getNextLinkStatuses());
  }

  public void markBroken() throws InvalidLinkStatusException {
    setLinkStatus(LinkStatus.BROKEN, getLinkStatus().getNextLinkStatuses());
  }

}
