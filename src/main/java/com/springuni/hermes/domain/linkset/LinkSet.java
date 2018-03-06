package com.springuni.hermes.domain.linkset;

import static java.util.Collections.unmodifiableList;

import com.springuni.hermes.domain.link.EmbeddedLink;
import com.springuni.hermes.domain.link.InvalidUrlException;
import com.springuni.hermes.domain.link.LongUrl;
import com.springuni.hermes.domain.user.Ownable;
import com.springuni.hermes.domain.utm.UtmParameters;
import com.springuni.hermes.domain.utm.UtmTemplate;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.Assert;

@Entity
public class LinkSet extends AbstractPersistable<Long> implements Ownable {

    private URL baseUrl;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "template_id")
    private UtmTemplate utmTemplate;

    @OneToMany(mappedBy = "linkSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmbeddedLink> embeddedLinks = new ArrayList<>();

    public LinkSet(String baseUrl, UtmTemplate utmTemplate, Long userId)
            throws InvalidUrlException {

        this(null, baseUrl, utmTemplate, userId);
    }

    public LinkSet(Long linkSetId, String baseUrl, UtmTemplate utmTemplate, Long userId)
            throws InvalidUrlException {

        Assert.notNull(baseUrl, "baseUrl cannot be null");
        Assert.notNull(utmTemplate, "utmTemplate cannot be null");
        Assert.notNull(userId, "userId cannot be null");

        setId(linkSetId);

        try {
            this.baseUrl = new URL(baseUrl);
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(e);
        }

        this.utmTemplate = utmTemplate;
        this.userId = userId;

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

    public List<EmbeddedLink> getEmbeddedLinks() {
        return unmodifiableList(embeddedLinks);
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public UtmTemplate getUtmTemplate() {
        return utmTemplate;
    }

    public void regenerateLinks() {
        embeddedLinks.clear();
        Set<UtmParameters> utmParametersSet = utmTemplate.getUtmParametersSet();
        utmParametersSet.forEach(utmParameters -> {
            embeddedLinks.add(new EmbeddedLink(LongUrl.from(baseUrl, utmParameters), this));
        });
    }

    public void updateBaseUrl(@NotNull String baseUrl) throws InvalidUrlException {
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

}
