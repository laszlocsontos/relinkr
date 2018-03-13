package com.springuni.hermes.link.model;

import static com.springuni.hermes.link.model.LinkType.EMBEDDED;

import com.springuni.hermes.utm.UtmParameters;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

@Entity
@DiscriminatorValue("E")
public class EmbeddedLink extends Link {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "linkset_id")
    private LinkSet linkSet;

    public EmbeddedLink(String baseUrl, @NotNull Long userId) throws InvalidUrlException {
        super(baseUrl, userId);
    }

    public EmbeddedLink(String baseUrl, UtmParameters utmParameters, @NotNull Long userId)
            throws InvalidUrlException {
        super(baseUrl, utmParameters, userId);
    }

    public EmbeddedLink(@NotNull LongUrl longUrl, @NotNull LinkSet linkSet, @NotNull Long userId) {
        super(longUrl, userId);
        this.linkSet = linkSet;
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    EmbeddedLink() {
        super();
    }

    public LinkSet getLinkSet() {
        return linkSet;
    }

    void setLinkSet(LinkSet linkSet) {
        Assert.notNull(linkSet, "linkSet cannot be null");
        this.linkSet = linkSet;
    }

    @Override
    public LinkType getLinkType() {
        return EMBEDDED;
    }

    @Override
    public LinkStatus getLinkStatus() {
        return linkSet.getLinkStatus();
    }

    @Override
    protected void setLinkStatus(LinkStatus linkStatus) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Tag> getTags() {
        return linkSet.getTags();
    }

    @Override
    public void addTag(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeTag(Tag tag) {
        throw new UnsupportedOperationException();
    }

}
