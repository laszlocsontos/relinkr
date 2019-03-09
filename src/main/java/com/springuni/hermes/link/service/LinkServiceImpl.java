package com.springuni.hermes.link.service;

import static com.springuni.hermes.link.model.LinkStatus.ACTIVE;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.core.model.EntityNotFoundException;
import com.springuni.hermes.link.model.InvalidUrlException;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.link.model.UtmParameters;
import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
class LinkServiceImpl
        extends AbstractLinkService<LinkId, Link, LinkRepository> implements LinkService {

    public LinkServiceImpl(LinkRepository linkRepository) {
        super(linkRepository);
    }

    @Override
    public URI getTargetUrl(String path) throws EntityNotFoundException {
        Assert.hasText(path, "path must contain text");

        Link link = linkRepository
                .findByPath(path)
                .orElseThrow(() -> new EntityNotFoundException("path", path));

        if (!ACTIVE.equals(link.getLinkStatus())) {
            throw new EntityNotFoundException("path", path);
        }

        return link.getTargetUrl();
    }

    @Override
    public Link addLink(String longUrl, UtmParameters utmParameters, UserId userId)
            throws InvalidUrlException {

        Assert.hasText(longUrl, "longUrl must contain text");
        Assert.notNull(userId, "userId cannot be null");

        Link link = new Link(longUrl, utmParameters, userId);
        return linkRepository.save(link);
    }

    @Override
    public Link updateLongUrl(LinkId linkId, String longUrl, UtmParameters utmParameters) {
        Assert.notNull(linkId, "linkId cannot be null");
        Assert.hasText(longUrl, "longUrl must contain text");

        Link link = getLink(linkId);
        verifyLinkBeforeUpdate(link);
        link.updateLongUrl(longUrl, utmParameters);
        return linkRepository.save(link);
    }

    @Override
    public Link updateUtmParameters(LinkId linkId, UtmParameters utmParameters)
            throws ApplicationException {

        Assert.notNull(linkId, "linkId cannot be null");

        Link link = getLink(linkId);
        verifyLinkBeforeUpdate(link);
        link.apply(utmParameters);
        return linkRepository.save(link);
    }

    @Override
    protected void verifyLinkBeforeUpdate(Link link) {
        // TODO: I don't remember what this was supposed to be doing
    }

}
