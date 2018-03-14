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
import com.springuni.hermes.utm.UtmParameters;
import java.net.URL;
import org.springframework.stereotype.Service;

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
    public URL getTargetUrl(String path) throws EntityNotFoundException {
        Link link = linkRepository
                .findByPath(path)
                .orElseThrow(() -> new EntityNotFoundException("path", path));

        if (!ACTIVE.equals(link.getLinkStatus())) {
            throw new EntityNotFoundException("path", path);
        }

        return link.getTargetUrl();
    }

    @Override
    public Link addLink(String baseUrl, UtmParameters utmParameters, Long userId)
            throws InvalidUrlException {
        StandaloneLink link = new StandaloneLink(baseUrl, utmParameters, userId);
        return standaloneLinkRepository.save(link);
    }

    @Override
    public Link updateLink(Long linkId, String baseUrl, UtmParameters utmParameters)
            throws ApplicationException {

        Link link = getLink(linkId);
        verifyLinkBeforeUpdate(link);
        link.updateLongUrl(baseUrl, utmParameters);
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
