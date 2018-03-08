package com.springuni.hermes.domain.linkset;

import static com.springuni.hermes.domain.link.LinkStatus.PENDING;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static javax.persistence.EnumType.STRING;

import com.springuni.hermes.domain.link.EmbeddedLink;
import com.springuni.hermes.domain.link.InvalidLinkStatusException;
import com.springuni.hermes.domain.link.InvalidUrlException;
import com.springuni.hermes.domain.link.LinkBase;
import com.springuni.hermes.domain.link.LinkStatus;
import com.springuni.hermes.domain.link.LongUrl;
import com.springuni.hermes.domain.link.Tag;
import com.springuni.hermes.domain.utm.UtmParameters;
import com.springuni.hermes.domain.utm.UtmTemplate;
import java.net.MalformedURLException;
import java.net.URL;
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
public class LinkSet extends LinkBase<Long> {

    private URL baseUrl;

    @Enumerated(STRING)
    private LinkStatus linkStatus = PENDING;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "template_id")
    private UtmTemplate utmTemplate;

    @OneToMany(mappedBy = "linkSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmbeddedLink> embeddedLinks = new ArrayList<>();

    @ElementCollection
    private Set<Tag> tags = new LinkedHashSet<>();

    public LinkSet(@NotNull String baseUrl, @NotNull UtmTemplate utmTemplate, @NotNull Long userId)
            throws InvalidUrlException {

        super(userId);

        Assert.notNull(baseUrl, "baseUrl cannot be null");
        Assert.notNull(utmTemplate, "utmTemplate cannot be null");

        try {
            this.baseUrl = new URL(baseUrl);
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(e);
        }

        this.utmTemplate = utmTemplate;

        regenerateLinks();
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    LinkSet() {
    }

    public URL getBaseUrl() {
        return baseUrl;
    }

    @Override
    public LinkStatus getLinkStatus() {
        return linkStatus;
    }

    @Override
    protected void setLinkStatus(LinkStatus linkStatus) {
        this.linkStatus = linkStatus;
    }

    @Override
    public void markActive() throws InvalidLinkStatusException {
        super.markActive();
    }

    @Override
    public void markArchived() throws InvalidLinkStatusException {
        super.markArchived();
    }

    @Override
    public void markBroken() throws InvalidLinkStatusException {
        super.markBroken();
    }

    public UtmTemplate getUtmTemplate() {
        return utmTemplate;
    }

    public List<EmbeddedLink> getEmbeddedLinks() {
        return unmodifiableList(embeddedLinks);
    }

    public void regenerateLinks() {
        embeddedLinks.clear();
        Set<UtmParameters> utmParametersSet = utmTemplate.getUtmParametersSet();
        utmParametersSet.forEach(utmParameters -> {
            embeddedLinks
                    .add(new EmbeddedLink(LongUrl.from(baseUrl, utmParameters), this, getUserId()));
        });
    }

    public void updateLongUrl(@NotNull String baseUrl) throws InvalidUrlException {
        try {
            this.baseUrl = new URL(baseUrl);
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(e);
        }

        for (EmbeddedLink embeddedLink : embeddedLinks) {
            embeddedLink.updateLongUrl(baseUrl);
        }
    }

    public void updateUtmTemplate(@NotNull UtmTemplate utmTemplate) {
        this.utmTemplate = utmTemplate;
        if (!embeddedLinks.isEmpty()) {
            regenerateLinks();
        }
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
