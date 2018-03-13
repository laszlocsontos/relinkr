package com.springuni.hermes.link;

import static com.springuni.hermes.link.LinkStatus.ACTIVE;
import static com.springuni.hermes.link.LinkStatus.ARCHIVED;
import static com.springuni.hermes.link.LinkStatus.BROKEN;
import static com.springuni.hermes.link.LinkStatus.PENDING;
import static java.util.Collections.emptySet;

import com.springuni.hermes.core.AbstractEntity;
import com.springuni.hermes.user.Ownable;
import java.io.Serializable;
import java.net.URL;
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

    public abstract URL getBaseUrl();

    public abstract void updateLongUrl(@NotNull String baseUrl) throws InvalidUrlException;

    public abstract Set<Tag> getTags();

    protected abstract void addTag(Tag tag);

    protected abstract void removeTag(Tag tag);

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

    protected void markActive() throws InvalidLinkStatusException {
        setLinkStatus(ACTIVE, getLinkStatus().getNextLinkStatuses());
    }

    protected void markArchived() throws InvalidLinkStatusException {
        setLinkStatus(ARCHIVED, getLinkStatus().getNextLinkStatuses());
    }

    protected void markBroken() throws InvalidLinkStatusException {
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
