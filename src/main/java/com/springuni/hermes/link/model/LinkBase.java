package com.springuni.hermes.link.model;

import static com.springuni.hermes.link.model.LinkStatus.ACTIVE;
import static com.springuni.hermes.link.model.LinkStatus.ARCHIVED;
import static com.springuni.hermes.link.model.LinkStatus.BROKEN;
import static com.springuni.hermes.link.model.LinkStatus.PENDING;
import static java.util.Collections.emptySet;

import com.springuni.hermes.core.orm.AbstractEntity;
import com.springuni.hermes.user.model.Ownable;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.MappedSuperclass;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

@MappedSuperclass
public abstract class LinkBase<PK extends Serializable>
        extends AbstractEntity<PK> implements Ownable {

    private Long userId;

    LinkBase(@NotNull Long userId) {
        Assert.notNull(userId, "userId cannot be null");
        this.userId = userId;
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    LinkBase() {
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public abstract URI getLongUrl();

    public abstract void updateLongUrl(@NotNull String longUrl) throws InvalidUrlException;

    public abstract Set<Tag> getTags();

    public abstract void addTag(Tag tag);

    public abstract void removeTag(Tag tag);

    public abstract LinkStatus getLinkStatus();

    protected abstract void setLinkStatus(LinkStatus linkStatus);

    public Set<LinkStatus> getUserLinkStatuses() {
        // Users cannot change state in PENDING state
        if (PENDING.equals(getLinkStatus())) {
            return emptySet();
        }

        return Collections.unmodifiableSet(
                getLinkStatus().getNextLinkStatuses().stream().filter(LinkStatus::isUserSettable)
                        .collect(Collectors.toSet())
        );
    }

    public void markActive() throws InvalidLinkStatusException {
        setLinkStatus(ACTIVE, getLinkStatus().getNextLinkStatuses());
    }

    public void markArchived() throws InvalidLinkStatusException {
        setLinkStatus(ARCHIVED, getLinkStatus().getNextLinkStatuses());
    }

    public void markBroken() throws InvalidLinkStatusException {
        setLinkStatus(BROKEN, getLinkStatus().getNextLinkStatuses());
    }

    private void setLinkStatus(LinkStatus linkStatus, Set<LinkStatus> expectedLinkStatuses)
            throws InvalidLinkStatusException {

        expectedLinkStatuses = getLinkStatus().getNextLinkStatuses();
        if (!expectedLinkStatuses.contains(linkStatus)) {
            throw InvalidLinkStatusException.forLinkStatus(linkStatus, expectedLinkStatuses);
        }

        setLinkStatus(linkStatus);
    }

}
