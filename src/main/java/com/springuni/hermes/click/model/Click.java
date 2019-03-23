package com.springuni.hermes.click.model;

import static javax.persistence.EnumType.STRING;

import com.springuni.hermes.core.model.Country;
import com.springuni.hermes.core.orm.AbstractEntity;
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.link.model.RedirectedEvent;
import com.springuni.hermes.user.model.Ownable;
import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.visitor.model.VisitorId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Entity
public class Click extends AbstractEntity<ClickId> implements Ownable {

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "link_id"))
    private LinkId linkId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "visitor_id"))
    private VisitorId visitorId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    private UserId userId;

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

        this.linkId = linkId;
        this.visitorId = visitorId;
        this.userId = userId;
        this.visitorIp = visitorIp;

        setVisitTimestamp(visitTimestamp);

        this.country = country;
    }

    private Click(Click click, Country country) {
        this(
                click.linkId, click.visitorId, click.userId, click.visitorIp, click.visitTimestamp,
                country
        );
    }

    public static Click of(
            @NonNull LinkId linkId,
            @NonNull VisitorId visitorId,
            @NonNull UserId userId,
            @NonNull IpAddress visitorIp,
            @NonNull LocalDateTime visitTimestamp) {

        return new Click(linkId, visitorId, userId, visitorIp, visitTimestamp, null);
    }

    public static Click from(RedirectedEvent redirectedEvent) throws InvalidIpAddressException {
        return new Click(
                redirectedEvent.getLinkId(),
                redirectedEvent.getVisitorId(),
                redirectedEvent.getUserId(),
                IpAddress.of("0.0.0.0"), // TODO: Add IP address to RedirectedEvent
                LocalDateTime.ofInstant(redirectedEvent.getInstant(), ZoneOffset.UTC),
                null
        );
    }

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
