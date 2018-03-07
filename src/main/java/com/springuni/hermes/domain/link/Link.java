package com.springuni.hermes.domain.link;

import static com.springuni.hermes.domain.link.LinkStatus.*;
import static javax.persistence.DiscriminatorType.CHAR;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import com.springuni.hermes.core.IdentityGenerator;
import com.springuni.hermes.domain.user.Ownable;
import com.springuni.hermes.domain.utm.UtmParameters;
import java.net.URL;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import org.hashids.Hashids;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = CHAR, name = "link_type")
@DiscriminatorValue("L")
public abstract class Link extends AbstractPersistable<Long> implements Ownable {

    private static final String HASHIDS_SALT = "6cY$S!08HpP$pWRpEErhGp7H3307a^67";
    static final int HASHIDS_LENGTH = 11;

    private static final String HASHIDS_ALPHABET =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";

    private static final Hashids HASHIDS =
            new Hashids(HASHIDS_SALT, HASHIDS_LENGTH, HASHIDS_ALPHABET);

    @Embedded
    protected LongUrl longUrl;

    protected Long userId;

    protected String path;

    @Enumerated(STRING)
    protected LinkStatus linkStatus = PENDING;

    public Link(String baseUrl) throws InvalidUrlException {
        this(baseUrl, null);
    }

    public Link(String baseUrl, UtmParameters utmParameters) throws InvalidUrlException {
        this(new LongUrl(baseUrl, utmParameters));
    }

    Link(LongUrl longUrl) {
        this.longUrl = longUrl;
        path = generatePath();
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    Link() {
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public URL getBaseUrl() {
        return longUrl.getBaseUrl();
    }

    public String getPath() {
        return path;
    }

    public void activate() {
        linkStatus = ACTIVE;
    }

    public void archive() {
        linkStatus = ARCHIVED;
    }

    public LinkStatus getLinkStatus() {
        return linkStatus;
    }

    public URL getTargetUrl() {
        return longUrl.getTargetUrl();
    }

    @Override
    public String toString() {
        return getTargetUrl().toString();
    }

    public void updateLongUrl(String baseUrl) throws InvalidUrlException {
        updateLongUrl(baseUrl, longUrl.getUtmParameters());
    }

    public void updateLongUrl(String baseUrl, UtmParameters utmParameters)
            throws InvalidUrlException {
        longUrl = new LongUrl(baseUrl, utmParameters);
    }

    private String generatePath() {
        long pathIdentity = IdentityGenerator.generate();
        return HASHIDS.encode(pathIdentity);
    }

}
