package com.springuni.hermes.domain.link;

import static javax.persistence.DiscriminatorType.CHAR;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import java.net.URL;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;
import org.springframework.data.jpa.domain.AbstractPersistable;

@MappedSuperclass
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = CHAR, name = "link_type")
abstract class Link extends AbstractPersistable<LinkId> {

    @Embedded
    protected LongUrl longUrl;

    public Link(String baseUrl) throws InvalidUrlException {
        longUrl = new LongUrl(baseUrl);
    }

    public Link(String baseUrl, UtmParameters utmParameters) throws InvalidUrlException {
        longUrl = new LongUrl(baseUrl, utmParameters);
    }

    Link(LongUrl longUrl) {
        this.longUrl = longUrl;
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    Link() {
    }

    @Override
    @EmbeddedId
    public LinkId getId() {
        return super.getId();
    }

    public URL getBaseUrl() {
        return longUrl.getBaseUrl();
    }

    public URL getTargetUrl() {
        return longUrl.getTargetUrl();
    }

    @Override
    public String toString() {
        return getTargetUrl().toString();
    }

    public void updateLongUrl(String baseUrl) throws InvalidUrlException {
        longUrl = new LongUrl(baseUrl, longUrl.getUtmParameters());
    }

}
