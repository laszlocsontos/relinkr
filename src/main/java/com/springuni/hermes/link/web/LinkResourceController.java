package com.springuni.hermes.link.web;

import static com.springuni.hermes.link.model.LinkStatus.ACTIVE;
import static com.springuni.hermes.link.model.LinkStatus.ARCHIVED;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.link.model.InvalidLinkStatusException;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkStatus;
import com.springuni.hermes.link.service.LinkService;
import java.util.EnumSet;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/links")
public class LinkResourceController {

    private final LinkService linkService;
    private final LinkResourceAssembler linkResourceAssembler;
    private final PagedResourcesAssembler pagedResourcesAssembler =
            new PagedResourcesAssembler(null, null);

    public LinkResourceController(LinkService linkService, LinkResourceAssembler linkResourceAssembler) {
        this.linkService = linkService;
        this.linkResourceAssembler = linkResourceAssembler;
    }

    @GetMapping(path = "/{linkId}", produces = HAL_JSON_VALUE)
    HttpEntity<LinkResource> getLink(@PathVariable long linkId) throws ApplicationException {
        Link link = linkService.getLink(linkId);
        return ok(linkResourceAssembler.toResource(link));
    }

    @PostMapping(produces = HAL_JSON_VALUE)
    HttpEntity<LinkResource> addLink(@Valid @RequestBody LinkResource linkResource)
            throws ApplicationException {

        Link link = linkService.addLink(
                linkResource.getBaseUrl(),
                linkResource.getUtmParameters(),
                1L // TODO
        );

        return ok(linkResourceAssembler.toResource(link));
    }

    @PutMapping(path = "/{linkId}", produces = HAL_JSON_VALUE)
    HttpEntity<LinkResource> updateLink(@PathVariable long linkId,
            @RequestBody LinkResource linkResource)
            throws ApplicationException {

        Link link = linkService.updateLink(
                linkId,
                linkResource.getBaseUrl(),
                linkResource.getUtmParameters()
        );

        return ok(linkResourceAssembler.toResource(link));
    }

    @GetMapping(produces = HAL_JSON_VALUE)
    HttpEntity<PagedResources<LinkResource>> listLinks(
            @RequestParam(required = false) Long userId, Pageable pageable) {

        Page<Link> linkPage = linkService.listLinks(1L, pageable);
        return ok(pagedResourcesAssembler.toResource(linkPage, linkResourceAssembler));
    }

    @PutMapping(path = "/{linkId}/linkStatuses/{linkStatus}")
    HttpEntity updateLinkStatus(@PathVariable long linkId, @PathVariable LinkStatus linkStatus)
            throws ApplicationException {

        switch (linkStatus) {
            case ACTIVE:
                linkService.activateLink(linkId);
                break;
            case ARCHIVED:
                linkService.archiveLink(linkId);
                break;
            default:
                throw InvalidLinkStatusException.forLinkStatus(
                        linkStatus, EnumSet.of(ACTIVE, ARCHIVED)
                );
        }

        return ok().build();
    }

    @PostMapping(path = "/{linkId}/tags/{tagName}")
    HttpEntity addTag(@PathVariable long linkId, @PathVariable String tagName)
            throws ApplicationException {

        linkService.addTag(linkId, tagName);

        return ok().build();
    }

    @DeleteMapping(path = "/{linkId}/tags/{tagName}")
    HttpEntity removeTag(@PathVariable long linkId, @PathVariable String tagName)
            throws ApplicationException {
        linkService.removeTag(linkId, tagName);

        return ok().build();
    }

}
