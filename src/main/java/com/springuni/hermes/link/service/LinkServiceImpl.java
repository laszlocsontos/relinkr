package com.springuni.hermes.link.service;

import static com.springuni.hermes.link.model.LinkStatus.ACTIVE;
import static com.springuni.hermes.link.model.LinkType.STANDALONE;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.core.model.EntityNotFoundException;
import com.springuni.hermes.link.model.InvalidUrlException;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkType;
import com.springuni.hermes.link.model.StandaloneLink;
import com.springuni.hermes.link.model.UnsupportedLinkOperationException;
import com.springuni.hermes.utm.model.UtmParameters;
import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
class LinkServiceImpl
        extends AbstractLinkService<Long, Link, LinkRepository<Link>> implements LinkService {

    private final StandaloneLinkRepository standaloneLinkRepository;

    public LinkServiceImpl(
            LinkRepository<Link> linkRepository,
            StandaloneLinkRepository standaloneLinkRepository) {

        super(linkRepository);

        this.standaloneLinkRepository = standaloneLinkRepository;
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
    public Link addLink(String longUrl, UtmParameters utmParameters, Long userId)
            throws InvalidUrlException {

        Assert.hasText(longUrl, "longUrl must contain text");
        Assert.notNull(userId, "userId cannot be null");

        StandaloneLink link = new StandaloneLink(longUrl, utmParameters, userId);
        return standaloneLinkRepository.save(link);
    }

    @Override
    public Link updateLongUrl(Long linkId, String longUrl, UtmParameters utmParameters) {
        Assert.notNull(linkId, "linkId cannot be null");
        Assert.hasText(longUrl, "longUrl must contain text");

        Link link = getLink(linkId);
        verifyLinkBeforeUpdate(link);
        link.updateLongUrl(longUrl, utmParameters);
        return standaloneLinkRepository.save((StandaloneLink) link);
    }

    @Override
    public Link updateUtmParameters(Long linkId, UtmParameters utmParameters)
            throws ApplicationException {

        Assert.notNull(linkId, "linkId cannot be null");

        Link link = getLink(linkId);
        verifyLinkBeforeUpdate(link);
        ((StandaloneLink) link).apply(utmParameters);
        return standaloneLinkRepository.save((StandaloneLink) link);
    }

    @Override
    protected void verifyLinkBeforeUpdate(Link link) {
        LinkType linkType = link.getLinkType();
        if (!STANDALONE.equals(linkType)) {
            throw UnsupportedLinkOperationException.forLinkType(linkType);
        }
    }

}
