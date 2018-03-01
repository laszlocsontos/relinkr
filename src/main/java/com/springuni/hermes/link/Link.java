package com.springuni.hermes.link;

import static java.util.Collections.unmodifiableSet;

import com.springuni.hermes.user.Ownable;
import com.springuni.hermes.user.UserId;
import java.net.URL;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Link extends AbstractPersistable<LinkId> implements Ownable {

    @Embedded
    private LongUrl longUrl;

    @Embedded
    private UserId owner;

    @ElementCollection
    private Set<Tag> tags;

    public void apply(UtmParameters utmParameters) {
        longUrl = longUrl.apply(utmParameters);
    }

    @Override
    @EmbeddedId
    public LinkId getId() {
        return super.getId();
    }

    @Override
    public UserId getOwner() {
        return owner;
    }

    public void setOwner(UserId owner) {
        this.owner = owner;
    }

    public URL getBaseUrl() {
        return longUrl.getBaseUrl();
    }

    public URL getTargetUrl() {
        return longUrl.getTargetUrl();
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

    public void updateBaseUrl(String baseUrl) throws InvalidLongUrlException {
        longUrl = new LongUrl(baseUrl, longUrl.getUtmParameters());
    }

    @Override
    public String toString() {
        return getTargetUrl().toString();
    }

}
