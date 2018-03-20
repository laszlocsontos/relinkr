package com.springuni.hermes.link.model;

import static com.springuni.hermes.link.model.LinkStatus.PENDING;
import static com.springuni.hermes.link.model.LinkType.STANDALONE;
import static java.util.Collections.unmodifiableSet;
import static javax.persistence.EnumType.STRING;

import com.springuni.hermes.user.model.Ownable;
import com.springuni.hermes.utm.UtmParameters;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity
@DiscriminatorValue("S")
public class StandaloneLink extends Link implements Ownable {

    @Enumerated(STRING)
    private LinkStatus linkStatus = PENDING;

    @ElementCollection
    private Set<Tag> tags = new LinkedHashSet<>();

    public StandaloneLink(@NotNull String baseUrl, @NotNull Long userId)
            throws InvalidUrlException {
        super(baseUrl, userId);
    }

    public StandaloneLink(@NotNull String baseUrl, @Nullable UtmParameters utmParameters,
            @NotNull Long userId)
            throws InvalidUrlException {
        super(baseUrl, utmParameters, userId);
    }

    StandaloneLink() {
        super();
    }

    StandaloneLink(@NotNull LongUrl longUrl, @NotNull Long userId) {
        super(longUrl, userId);
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
    public LinkType getLinkType() {
        return STANDALONE;
    }

    @Override
    public LinkStatus getLinkStatus() {
        return linkStatus;
    }

    @Override
    protected void setLinkStatus(LinkStatus linkStatus) {
        this.linkStatus = linkStatus;
    }

    @Override
    public void markActive() throws InvalidLinkStatusException {
        super.markActive();
    }

    @Override
    public void markArchived() throws InvalidLinkStatusException {
        super.markArchived();
    }

    @Override
    public void markBroken() throws InvalidLinkStatusException {
        super.markBroken();
    }

}
