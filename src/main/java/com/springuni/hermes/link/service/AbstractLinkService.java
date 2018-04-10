package com.springuni.hermes.link.service;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.core.model.EntityNotFoundException;
import com.springuni.hermes.core.orm.AbstractId;
import com.springuni.hermes.core.orm.OwnableRepository;
import com.springuni.hermes.link.model.LinkBase;
import com.springuni.hermes.link.model.Tag;
import com.springuni.hermes.user.model.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

public abstract class AbstractLinkService<ID extends AbstractId<L>, L extends LinkBase<ID>, R extends OwnableRepository<L, ID>>
        implements LinkBaseService<ID, L> {

    final R linkRepository;

    AbstractLinkService(R linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public L getLink(ID id) throws EntityNotFoundException {
        Assert.notNull(id, "id cannot be null");

        return linkRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("id", id));

    }

    @Override
    public Page<L> listLinks(UserId userId, Pageable pageable) {
        return linkRepository.findByUserId(userId, pageable);
    }

    @Override
    public void activateLink(ID id) throws ApplicationException {
        Assert.notNull(id, "id cannot be null");

        L link = getLink(id);
        verifyLinkBeforeUpdate(link);
        link.markActive();

        linkRepository.save(link);
    }

    @Override
    public void archiveLink(ID id) throws ApplicationException {
        Assert.notNull(id, "id cannot be null");

        L link = getLink(id);
        verifyLinkBeforeUpdate(link);
        link.markArchived();

        linkRepository.save(link);
    }

    @Override
    public void addTag(ID id, String tagName) throws ApplicationException {
        Assert.notNull(id, "id cannot be null");
        Assert.hasText(tagName, "tagName must contain text");

        L link = getLink(id);
        verifyLinkBeforeUpdate(link);
        link.addTag(new Tag(tagName));

        linkRepository.save(link);
    }

    @Override
    public void removeTag(ID id, String tagName) throws ApplicationException {
        Assert.notNull(id, "id cannot be null");
        Assert.hasText(tagName, "tagName must contain text");

        L link = getLink(id);
        verifyLinkBeforeUpdate(link);
        link.removeTag(new Tag(tagName));
        linkRepository.save(link);
    }

    @Override
    public L updateLongUrl(ID id, String longUrl) {
        Assert.notNull(id, "id cannot be null");
        Assert.hasText(longUrl, "longUrl must contain text");

        L link = getLink(id);
        link.updateLongUrl(longUrl);
        return linkRepository.save(link);
    }

    protected void verifyLinkBeforeUpdate(L link) {
    }

}
