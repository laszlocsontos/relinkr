package com.springuni.hermes.link.service;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.core.model.EntityNotFoundException;
import com.springuni.hermes.core.orm.OwnableRepository;
import com.springuni.hermes.link.model.LinkBase;
import com.springuni.hermes.link.model.Tag;
import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class AbstractLinkService<ID extends Serializable, L extends LinkBase<ID>, R extends OwnableRepository<L, ID>>
        implements LinkBaseService<ID, L> {

    final R linkRepository;

    AbstractLinkService(R linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public L getLink(ID id) throws EntityNotFoundException {
        return linkRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("id", id));

    }

    @Override
    public Page<L> listLinks(long userId, Pageable pageable) {
        return linkRepository.findByUserId(userId, pageable);
    }

    @Override
    public void activateLink(ID id) throws ApplicationException {
        L link = getLink(id);
        verifyLinkBeforeUpdate(link);
        link.markActive();
        linkRepository.save(link);
    }

    @Override
    public void archiveLink(ID id) throws ApplicationException {
        L link = getLink(id);
        verifyLinkBeforeUpdate(link);
        link.markArchived();
        linkRepository.save(link);
    }

    @Override
    public void addTag(ID id, String tagName) throws ApplicationException {
        L link = getLink(id);
        verifyLinkBeforeUpdate(link);
        link.addTag(new Tag(tagName));
        linkRepository.save(link);
    }

    @Override
    public void removeTag(ID id, String tagName) throws ApplicationException {
        L link = getLink(id);
        verifyLinkBeforeUpdate(link);
        link.removeTag(new Tag(tagName));
        linkRepository.save(link);
    }

    protected void verifyLinkBeforeUpdate(L link) {
    }

}
