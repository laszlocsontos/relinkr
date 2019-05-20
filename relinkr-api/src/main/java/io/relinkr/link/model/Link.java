package io.relinkr.link.model;

import static io.relinkr.link.model.LinkStatus.PENDING;
import static java.util.Collections.unmodifiableSet;
import static javax.persistence.EnumType.STRING;

import io.relinkr.core.util.IdentityGenerator;
import io.relinkr.user.model.UserId;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import lombok.NonNull;
import org.hashids.Hashids;

@Entity
public class Link extends LinkBase<LinkId> {

    static final int HASHIDS_LENGTH = 11;
    private static final String HASHIDS_SALT = "6cY$S!08HpP$pWRpEErhGp7H3307a^67";
    private static final String HASHIDS_ALPHABET =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";

    private static final Hashids HASHIDS =
            new Hashids(HASHIDS_SALT, HASHIDS_LENGTH, HASHIDS_ALPHABET);

    @Embedded
    protected LongUrl longUrl;

    @Column(name = "path_")
    private String path;

    @Enumerated(STRING)
    private LinkStatus linkStatus = PENDING;

    @ElementCollection
    private Set<Tag> tags = new LinkedHashSet<>();

    public Link(
            @NonNull String longUrl, UtmParameters utmParameters, @NonNull UserId userId)
            throws InvalidUrlException {

        this(new LongUrl(longUrl, utmParameters), userId);
    }

    public Link(@NonNull String longUrl, @NonNull UserId userId) throws InvalidUrlException {
        this(longUrl, null, userId);
    }

    Link(@NonNull LongUrl longUrl, @NonNull UserId userId) {
        super(userId);
        this.longUrl = longUrl;
        path = generatePath();
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    Link() {
    }

    public URI getLongUrl() {
        return longUrl.getLongUrl();
    }

    public String getPath() {
        return path;
    }

    public Optional<UtmParameters> getUtmParameters() {
        return longUrl.getUtmParameters();
    }

    public URI getTargetUrl() {
        return longUrl.getTargetUrl();
    }

    @Override
    public LinkStatus getLinkStatus() {
        return linkStatus;
    }

    @Override
    protected void setLinkStatus(LinkStatus linkStatus) {
        this.linkStatus = linkStatus;
    }

    public void apply(UtmParameters utmParameters) {
        longUrl = longUrl.apply(utmParameters);
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

    @Override
    public String toString() {
        return getTargetUrl().toString();
    }

    public void updateLongUrl(@NonNull String longUrl) throws InvalidUrlException {
        updateLongUrl(longUrl, this.longUrl.getUtmParameters().orElse(null));
    }

    public void updateLongUrl(String longUrl, UtmParameters utmParameters)
            throws InvalidUrlException {
        this.longUrl = new LongUrl(longUrl, utmParameters);
    }

    private String generatePath() {
        long pathIdentity = IdentityGenerator.getInstance().generate();
        return HASHIDS.encode(pathIdentity);
    }

}
