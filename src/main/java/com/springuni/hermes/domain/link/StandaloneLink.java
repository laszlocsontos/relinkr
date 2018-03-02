package com.springuni.hermes.domain.link;

import static java.util.Collections.unmodifiableSet;

import com.springuni.hermes.domain.user.Ownable;
import com.springuni.hermes.domain.user.UserId;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("S")
public class StandaloneLink extends Link implements Ownable {

    @Embedded
    private UserId owner;

    @ElementCollection
    private Set<Tag> tags = new LinkedHashSet<>();

    public StandaloneLink(String baseUrl) throws InvalidLongUrlException {
        super(baseUrl);
    }

    public StandaloneLink(String baseUrl, UtmParameters utmParameters)
            throws InvalidLongUrlException {
        super(baseUrl, utmParameters);
    }

    StandaloneLink() {
        super();
    }

    StandaloneLink(LongUrl longUrl) {
        super(longUrl);
    }

    public void apply(UtmParameters utmParameters) {
        longUrl = longUrl.apply(utmParameters);
    }

    @Override
    public UserId getOwner() {
        return owner;
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

}
