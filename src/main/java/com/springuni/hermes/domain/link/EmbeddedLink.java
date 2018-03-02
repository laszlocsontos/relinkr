package com.springuni.hermes.domain.link;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("E")
public class EmbeddedLink extends Link {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linkset_id")
    private LinkSet linkSet;

    public LinkSet getLinkSet() {
        return linkSet;
    }

    public EmbeddedLink(String baseUrl) throws InvalidLongUrlException {
        super(baseUrl);
    }

    public EmbeddedLink(String baseUrl, UtmParameters utmParameters)
            throws InvalidLongUrlException {
        super(baseUrl, utmParameters);
    }

    EmbeddedLink(LongUrl longUrl) {
        super(longUrl);
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    EmbeddedLink() {
        super();
    }

}
