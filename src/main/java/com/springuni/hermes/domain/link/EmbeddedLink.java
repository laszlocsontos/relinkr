package com.springuni.hermes.domain.link;

import com.springuni.hermes.domain.linkset.LinkSet;
import com.springuni.hermes.domain.utm.UtmParameters;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.jetbrains.annotations.NotNull;

@Entity
@DiscriminatorValue("E")
public class EmbeddedLink extends Link {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "linkset_id")
    private LinkSet linkSet;

    public LinkSet getLinkSet() {
        return linkSet;
    }

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

    void setLinkSet(LinkSet linkSet) {
        this.linkSet = linkSet;
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
    protected void addTag(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeTag(Tag tag) {
        throw new UnsupportedOperationException();
    }

}
