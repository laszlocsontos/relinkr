package com.springuni.hermes.domain.click;

import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;

import com.springuni.hermes.domain.link.LinkId;
import com.springuni.hermes.domain.visitor.VisitorId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.Assert;

@Entity
public class Click extends AbstractPersistable<ClickId> {

    @Embedded
    private VisitorId visitorId;

    @Embedded
    private IpAddress visitorIp;

    @Enumerated
    private Country country;

    @Embedded
    private LinkId linkId;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime visitTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
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

    @Override
    @EmbeddedId
    public ClickId getId() {
        return super.getId();
    }

    public Optional<Country> getCountry() {
        return Optional.ofNullable(country);
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

    public void setCountry(@NotNull Country country) {
        Assert.notNull(country, "country cannot be null");
        this.country = country;
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
