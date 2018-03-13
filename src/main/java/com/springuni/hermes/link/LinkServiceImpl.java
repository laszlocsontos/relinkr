package com.springuni.hermes.link;

import static com.springuni.hermes.link.LinkStatus.ACTIVE;
import static com.springuni.hermes.link.LinkType.STANDALONE;

import com.springuni.hermes.core.ApplicationException;
import com.springuni.hermes.core.EntityNotFoundException;
import com.springuni.hermes.utm.UtmParameters;
import java.net.URL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class LinkServiceImpl implements LinkService {

    private final LinkRepository<Link> linkRepository;
    private final StandaloneLinkRepository standaloneLinkRepository;

    public LinkServiceImpl(
            LinkRepository<Link> linkRepository,
            StandaloneLinkRepository standaloneLinkRepository) {

        this.linkRepository = linkRepository;
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
    public Link getLink(long linkId) throws EntityNotFoundException {
        return linkRepository
                .findById(linkId)
                .orElseThrow(() -> new EntityNotFoundException("id", linkId));
    }

    @Override
    public Link addLink(String baseUrl, UtmParameters utmParameters, Long userId)
            throws InvalidUrlException {
        StandaloneLink link = new StandaloneLink(baseUrl, utmParameters, userId);
        return standaloneLinkRepository.save(link);
    }

    @Override
    public Link updateLink(long linkId, String baseUrl, UtmParameters utmParameters)
            throws ApplicationException {

        Link link = getLink(linkId);
        expectStandaloneLink(link);
        link.updateLongUrl(baseUrl, utmParameters);
        return standaloneLinkRepository.save((StandaloneLink) link);
    }

    @Override
    public Page<Link> listLinks(long userId, Pageable pageable) {
        return linkRepository.findByUserId(userId, pageable);
    }

    @Override
    public void activateLink(long linkId) throws ApplicationException {
        Link link = getLink(linkId);
        expectStandaloneLink(link);
        link.markActive();
        standaloneLinkRepository.save((StandaloneLink) link);
    }

    @Override
    public void archiveLink(long linkId) throws ApplicationException {
        Link link = getLink(linkId);
        expectStandaloneLink(link);
        link.markArchived();
        standaloneLinkRepository.save((StandaloneLink) link);
    }

    @Override
    public void addTag(long linkId, String tagName) throws ApplicationException {
        Link link = getLink(linkId);
        expectStandaloneLink(link);
        link.addTag(new Tag(tagName));
        standaloneLinkRepository.save((StandaloneLink) link);
    }

    @Override
    public void removeTag(long linkId, String tagName) throws ApplicationException {
        Link link = getLink(linkId);
        expectStandaloneLink(link);
        link.removeTag(new Tag(tagName));
        standaloneLinkRepository.save((StandaloneLink) link);
    }

    private void expectStandaloneLink(Link link) throws UnsupportedLinkOperationException {
        LinkType linkType = link.getLinkType();
        if (!STANDALONE.equals(linkType)) {
            throw UnsupportedLinkOperationException.forLinkType(linkType);
        }
    }

}
