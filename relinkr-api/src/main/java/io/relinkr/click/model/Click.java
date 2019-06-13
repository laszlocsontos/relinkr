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

package io.relinkr.click.model;

import static javax.persistence.EnumType.STRING;

import io.relinkr.core.model.Country;
import io.relinkr.core.orm.OwnableEntity;
import io.relinkr.link.model.LinkId;
import io.relinkr.core.model.UserId;
import io.relinkr.visitor.model.VisitorId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a single click to a {@link io.relinkr.link.model.Link} performed by a given
 * {@link io.relinkr.visitor.model.Visitor} from an {@link IpAddress} at a specific point in time.
 */
@Getter
@Entity
public class Click extends OwnableEntity<ClickId> {

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "link_id"))
  private LinkId linkId;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "visitor_id"))
  private VisitorId visitorId;

  @Embedded
  private IpAddress visitorIp;

  @Enumerated(STRING)
  private Country country;

  private LocalDateTime visitTimestamp;
  private LocalDate visitDate;

  private int visitDayOfWeek;
  private int visitDayOfMonth;
  private int visitHour;
  private int visitMonth;

  /*
   * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
   */
  Click() {
  }

  private Click(
      LinkId linkId, VisitorId visitorId, UserId userId, IpAddress visitorIp,
      LocalDateTime visitTimestamp, Country country) {

    super(userId);

    this.linkId = linkId;
    this.visitorId = visitorId;
    this.visitorIp = visitorIp;

    setVisitTimestamp(visitTimestamp);

    this.country = country;
  }

  private Click(Click click, Country country) {
    this(
        click.linkId, click.visitorId, click.getUserId(), click.visitorIp, click.visitTimestamp,
        country
    );
  }

  /**
   * Creates a new {@code Click} instance with the following data.
   *
   * @param linkId {@code Link}'s ID
   * @param visitorId {@code Visitor}'s ID
   * @param userId {@code User}'s ID who own the link clicked
   * @param visitorIp {@code Visitor}'s IP address
   * @param visitTimestamp timestamp of the {@code Click}
   * @return a new {@code Click} instance
   * @throws IllegalArgumentException if any of the parameters above is null
   */
  public static Click of(
      @NonNull LinkId linkId,
      @NonNull VisitorId visitorId,
      @NonNull UserId userId,
      @NonNull IpAddress visitorIp,
      @NonNull LocalDateTime visitTimestamp) {

    return new Click(linkId, visitorId, userId, visitorIp, visitTimestamp, null);
  }

  /**
   * Makes a copy of {@code this} Click object and adds a {@link Country} to it.
   *
   * @param country {@code Country} to be added
   * @return a copy of {@code this} Click object
   */
  public Click with(Country country) {
    return new Click(this, country);
  }

  public Optional<Country> getCountry() {
    return Optional.ofNullable(country);
  }

  private void setVisitTimestamp(LocalDateTime visitTimestamp) {
    this.visitTimestamp = visitTimestamp;
    visitDate = LocalDate.from(visitTimestamp);
    visitDayOfMonth = visitTimestamp.getDayOfMonth();
    visitDayOfWeek = visitTimestamp.getDayOfWeek().getValue();
    visitHour = visitTimestamp.getHour();
    visitMonth = visitTimestamp.getMonth().getValue();
  }

}
