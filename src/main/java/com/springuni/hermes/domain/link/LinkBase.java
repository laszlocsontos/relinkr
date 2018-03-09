package com.springuni.hermes.domain.link;

import static com.springuni.hermes.domain.link.LinkStatus.*;

import com.springuni.hermes.domain.user.Ownable;
import java.io.Serializable;
import java.net.URL;
import java.util.EnumSet;
import java.util.Set;
import javax.persistence.MappedSuperclass;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.Assert;

@MappedSuperclass
public abstract class LinkBase<PK extends Serializable> extends AbstractPersistable<PK> implements Ownable {

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

    protected void markActive() throws InvalidLinkStatusException {
        setLinkStatus(ACTIVE, EnumSet.of(PENDING, BROKEN));
    }

    protected void markArchived() throws InvalidLinkStatusException {
        setLinkStatus(ARCHIVED, EnumSet.of(ACTIVE, BROKEN));
    }

    protected void markBroken() throws InvalidLinkStatusException {
        setLinkStatus(BROKEN, EnumSet.of(PENDING, ACTIVE));
    }

    private void setLinkStatus(LinkStatus linkStatus, Set<LinkStatus> expectedLinkStatuses)
            throws InvalidLinkStatusException {

        if (!expectedLinkStatuses.contains(getLinkStatus())) {
            throw InvalidLinkStatusException.forLinkStatus(linkStatus);
        }

        setLinkStatus(linkStatus);
    }

}
