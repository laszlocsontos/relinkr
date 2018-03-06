package com.springuni.hermes.domain.link;

import com.springuni.hermes.domain.linkset.LinkSet;
import com.springuni.hermes.domain.utm.UtmParameters;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("E")
public class EmbeddedLink extends Link {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "linkset_id")
    private LinkSet linkSet;

    public LinkSet getLinkSet() {
        return linkSet;
    }

    public EmbeddedLink(String baseUrl) throws InvalidUrlException {
        super(baseUrl);
    }

    public EmbeddedLink(String baseUrl, UtmParameters utmParameters)
            throws InvalidUrlException {
        super(baseUrl, utmParameters);
    }

    public EmbeddedLink(LongUrl longUrl, LinkSet linkSet) {
        super(longUrl);
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

}
