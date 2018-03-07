package com.springuni.hermes.domain.link;

import static com.springuni.hermes.domain.link.LinkStatus.*;

import com.springuni.hermes.core.ApplicationException;
import com.springuni.hermes.core.EntityNotFoundException;
import com.springuni.hermes.domain.user.UserService;
import com.springuni.hermes.domain.utm.UtmParameters;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class LinkServiceImpl implements LinkService {

    private final EmbeddedLinkRepository embeddedLinkRepository;
    private final StandaloneLinkRepository standaloneLinkRepository;
    private final UserService userService;

    public LinkServiceImpl(
            EmbeddedLinkRepository embeddedLinkRepository,
            StandaloneLinkRepository standaloneLinkRepository,
            UserService userService) {

        this.embeddedLinkRepository = embeddedLinkRepository;
        this.standaloneLinkRepository = standaloneLinkRepository;
        this.userService = userService;
    }

    @Override
    public URL getTargetUrl(String path) throws EntityNotFoundException {
        Link link = doGetLink(
                "path",
                path,
                embeddedLinkRepository::findByPath,
                standaloneLinkRepository::findByPath
        );

        if (!ACTIVE.equals(link.getLinkStatus())) {
            throw new EntityNotFoundException("path", path);
        }

        return link.getTargetUrl();
    }

    @Override
    public Link getLink(long linkId) throws EntityNotFoundException {
        return doGetLink(
                "id",
                linkId,
                embeddedLinkRepository::findById,
                standaloneLinkRepository::findById
        );
    }

    private <V> Link doGetLink(
            String fieldName, V fieldValue,
            Function<V, Optional<EmbeddedLink>> embeddedLinkFinder,
            Function<V, Optional<StandaloneLink>> standaloneLinkFinder)
            throws EntityNotFoundException {

        Optional<? extends Link> linkOptional = embeddedLinkFinder.apply(fieldValue);
        if (!linkOptional.isPresent()) {
            linkOptional = standaloneLinkFinder.apply(fieldValue);
        }
        if (!linkOptional.isPresent()) {
            throw new EntityNotFoundException(fieldName, fieldValue);
        }

        return linkOptional.get();
    }

    @Override
    public Link addLink(String baseUrl, UtmParameters utmParameters) throws InvalidUrlException {
        StandaloneLink link = new StandaloneLink(baseUrl, utmParameters);
        return standaloneLinkRepository.save(link);
    }

    @Override
    public Link updateLink(long linkId, String baseUrl, UtmParameters utmParameters)
            throws ApplicationException {

        StandaloneLink standaloneLink = getStandaloneLink(linkId);
        standaloneLink.updateLongUrl(baseUrl, utmParameters);
        return standaloneLinkRepository.save(standaloneLink);
    }

    @Override
    public Page<StandaloneLink> listLinks(long userId, Pageable pageable) {
        return standaloneLinkRepository.findByUserId(userId, pageable);
    }

    @Override
    public void archiveLink(long linkId) throws EntityNotFoundException {
        StandaloneLink standaloneLink = getStandaloneLink(linkId);
        standaloneLink.archive();
        standaloneLinkRepository.save(standaloneLink);
    }

    @Override
    public void addTag(long linkId, String tagName) throws ApplicationException {
        StandaloneLink standaloneLink = getStandaloneLink(linkId);
        standaloneLink.addTag(new Tag(tagName));
        standaloneLinkRepository.save(standaloneLink);
    }

    @Override
    public void removeTag(long linkId, String tagName) throws EntityNotFoundException {
        StandaloneLink standaloneLink = getStandaloneLink(linkId);
        standaloneLink.removeTag(new Tag(tagName));
        standaloneLinkRepository.save(standaloneLink);
    }

    private StandaloneLink getStandaloneLink(long linkId) throws EntityNotFoundException {
        return standaloneLinkRepository
                .findById(linkId)
                .orElseThrow(() -> new EntityNotFoundException("id", linkId));
    }

}
