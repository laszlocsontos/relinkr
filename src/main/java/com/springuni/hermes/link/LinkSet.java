package com.springuni.hermes.link;

import com.springuni.hermes.user.Ownable;
import com.springuni.hermes.user.UserId;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

public class LinkSet extends AbstractPersistable<LinkSetId> implements Ownable {

    private URL baseUrl;

    @Embedded
    private UserId owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private UtmTemplate utmTemplate;

    @JoinColumn(name = "link_id")
    @OneToMany(mappedBy = "linkSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Link> links = new ArrayList<>();

    @EmbeddedId
    @Override
    public LinkSetId getId() {
        return super.getId();
    }

    public URL getBaseUrl() {
        return baseUrl;
    }

    @Override
    public UserId getOwner() {
        return owner;
    }

    public UtmTemplate getUtmTemplate() {
        return utmTemplate;
    }

    public void regenerateLinks() {
        links.clear();
        Set<UtmParameters> utmParametersSet = utmTemplate.getUtmParameters();
        utmParametersSet.forEach(utmParameters -> {
            links.add(new EmbeddedLink(LongUrl.from(baseUrl, utmParameters)));
        });
    }

    public void updateBaseUrl(@NotNull String baseUrl) throws InvalidLongUrlException {
        try {
            this.baseUrl = new URL(baseUrl);
        } catch (MalformedURLException e) {
            throw new InvalidLongUrlException(e);
        }

        for (Link link : links) {
            link.updateLongUrl(baseUrl);
        }
    }

    public void updateUtmTemplate(@NotNull UtmTemplate utmTemplate) {
        this.utmTemplate = utmTemplate;
        if (!links.isEmpty()) {
            regenerateLinks();
        }
    }

}
