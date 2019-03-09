package com.springuni.hermes.link.model;

import static com.springuni.hermes.link.model.LinkStatus.PENDING;
import static java.util.Collections.unmodifiableSet;
import static javax.persistence.EnumType.STRING;

import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.utm.model.UtmTemplate;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

@Entity
public class LinkSet extends LinkBase<LinkSetId> {

    private URI longUrl;

    @Enumerated(STRING)
    private LinkStatus linkStatus = PENDING;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "template_id")
    private UtmTemplate utmTemplate;

    @ElementCollection
    private Set<Tag> tags = new LinkedHashSet<>();

    public LinkSet(@NotNull String longUrl, @NotNull UtmTemplate utmTemplate,
            @NotNull UserId userId)
            throws InvalidUrlException {

        super(userId);

        Assert.notNull(longUrl, "longUrl cannot be null");
        Assert.notNull(utmTemplate, "utmTemplate cannot be null");

        setLongUrl(longUrl);

        this.utmTemplate = utmTemplate;
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    LinkSet() {
    }

    public URI getLongUrl() {
        return longUrl;
    }

    private void setLongUrl(@NotNull String longUrl) {
        this.longUrl = new LongUrl(longUrl).getLongUrl();
    }

    @Override
    public LinkStatus getLinkStatus() {
        return linkStatus;
    }

    @Override
    protected void setLinkStatus(LinkStatus linkStatus) {
        this.linkStatus = linkStatus;
    }

    public UtmTemplate getUtmTemplate() {
        return utmTemplate;
    }

    public void updateLongUrl(@NotNull String longUrl) throws InvalidUrlException {
        setLongUrl(longUrl);

    }

    public Set<Tag> getTags() {
        return unmodifiableSet(tags);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

}
