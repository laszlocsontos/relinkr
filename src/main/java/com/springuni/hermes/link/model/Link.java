package com.springuni.hermes.link.model;

import static javax.persistence.DiscriminatorType.CHAR;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import com.springuni.hermes.core.util.IdentityGenerator;
import com.springuni.hermes.core.util.RandomGenerator;
import com.springuni.hermes.utm.UtmParameters;
import java.net.URL;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import org.hashids.Hashids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = CHAR, name = "link_type")
@DiscriminatorValue("L")
public abstract class Link extends LinkBase<Long> {

    static final int HASHIDS_LENGTH = 11;
    private static final String HASHIDS_SALT = "6cY$S!08HpP$pWRpEErhGp7H3307a^67";
    private static final String HASHIDS_ALPHABET =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";

    private static final Hashids HASHIDS =
            new Hashids(HASHIDS_SALT, HASHIDS_LENGTH, HASHIDS_ALPHABET);

    @Embedded
    protected LongUrl longUrl;

    private String path;

    Link(@NotNull String baseUrl, @NotNull Long userId) throws InvalidUrlException {
        this(baseUrl, null, userId);
    }

    Link(@NotNull String baseUrl, @Nullable UtmParameters utmParameters,
            @NotNull Long userId) throws InvalidUrlException {
        this(new LongUrl(baseUrl, utmParameters), userId);
    }

    Link(@NotNull LongUrl longUrl, @NotNull Long userId) {
        super(userId);
        this.longUrl = longUrl;
        path = generatePath();
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    Link() {
    }

    public URL getBaseUrl() {
        return longUrl.getBaseUrl();
    }

    public String getPath() {
        return path;
    }

    public UtmParameters getUtmParameters() {
        return longUrl.getUtmParameters();
    }

    public URL getTargetUrl() {
        return longUrl.getTargetUrl();
    }

    public abstract LinkType getLinkType();

    @Override
    public String toString() {
        return getTargetUrl().toString();
    }

    public void updateLongUrl(@NotNull String baseUrl) throws InvalidUrlException {
        updateLongUrl(baseUrl, longUrl.getUtmParameters());
    }

    public void updateLongUrl(String baseUrl, UtmParameters utmParameters)
            throws InvalidUrlException {
        longUrl = new LongUrl(baseUrl, utmParameters);
    }

    private String generatePath() {
        long pathIdentity = IdentityGenerator.getInstance().generate();
        return HASHIDS.encode(pathIdentity);
    }

}
