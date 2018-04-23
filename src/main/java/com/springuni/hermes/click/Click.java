package com.springuni.hermes.click;

import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;
import static javax.persistence.EnumType.STRING;

import com.springuni.hermes.core.model.Country;
import com.springuni.hermes.core.orm.AbstractEntity;
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.visitor.model.VisitorId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

@Entity
public class Click extends AbstractEntity<ClickId> {

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "visitor_id"))
    private VisitorId visitorId;

    @Embedded
    private IpAddress visitorIp;

    @Enumerated(STRING)
    private Country country;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "link_id"))
    private LinkId linkId;

    private LocalDateTime visitTimestamp;
    private LocalDate visitDate;

    private int visitDayOfWeek;
    private int visitDayOfMonth;
    private int visitHour;
    private int visitMonth;

    public Click(
            @NotNull VisitorId visitorId, @NotNull LinkId linkId, @NotNull IpAddress visitorIp) {

        this(visitorId, linkId, visitorIp, null);
    }

    public Click(
            @NotNull VisitorId visitorId, @NotNull LinkId linkId, @NotNull IpAddress visitorIp,
            @Nullable LocalDateTime visitTimestamp) {

        Assert.notNull(visitorId, "visitorId cannot be null");
        Assert.notNull(linkId, "linkId cannot be null");
        Assert.notNull(visitorIp, "visitorIp cannot be null");

        this.visitorId = visitorId;
        this.linkId = linkId;
        this.visitorIp = visitorIp;

        if (visitTimestamp != null) {
            setVisitTimestamp(visitTimestamp);
        } else {
            setVisitTimestamp(now(UTC));
        }
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    Click() {
    }

    public Optional<Country> getCountry() {
        return Optional.ofNullable(country);
    }

    public void setCountry(@NotNull Country country) {
        Assert.notNull(country, "country cannot be null");
        this.country = country;
    }

    public VisitorId getVisitorId() {
        return visitorId;
    }

    public IpAddress getVisitorIp() {
        return visitorIp;
    }

    public LinkId getLinkId() {
        return linkId;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public int getVisitDayOfWeek() {
        return visitDayOfWeek;
    }

    public int getVisitDayOfMonth() {
        return visitDayOfMonth;
    }

    public int getVisitMonth() {
        return visitMonth;
    }

    public int getVisitHour() {
        return visitHour;
    }

    public LocalDateTime getVisitTimestamp() {
        return visitTimestamp;
    }

    public void setVisitTimestamp(@NotNull LocalDateTime visitTimestamp) {
        Assert.notNull(visitTimestamp, "visitTimestamp cannot be null");

        this.visitTimestamp = visitTimestamp;
        visitDate = LocalDate.from(visitTimestamp);
        visitDayOfMonth = visitTimestamp.getDayOfMonth();
        visitDayOfWeek = visitTimestamp.getDayOfWeek().getValue();
        visitHour = visitTimestamp.getHour();
        visitMonth = visitTimestamp.getMonth().getValue();
    }

}
