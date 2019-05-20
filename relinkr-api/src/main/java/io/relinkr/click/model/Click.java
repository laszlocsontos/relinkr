package io.relinkr.click.model;

import static javax.persistence.EnumType.STRING;

import io.relinkr.core.model.Country;
import io.relinkr.core.orm.AbstractEntity;
import io.relinkr.link.model.LinkId;
import io.relinkr.user.model.Ownable;
import io.relinkr.user.model.UserId;
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
    private IpAddress visitorIp;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    private UserId userId;


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
