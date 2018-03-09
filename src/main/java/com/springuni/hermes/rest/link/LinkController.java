package com.springuni.hermes.rest.link;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

import com.springuni.hermes.core.ApplicationException;
import com.springuni.hermes.core.EntityNotFoundException;
import com.springuni.hermes.domain.link.Link;
import com.springuni.hermes.domain.link.LinkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping(path = "/{id}", produces = HAL_JSON_VALUE)
    LinkResource getLink(@PathVariable long linkId) {
        return null;
    }

    @GetMapping(path = "/", produces = HAL_JSON_VALUE)
    Page<LinkResource> listLinks(@RequestParam(required = false) long userId, Pageable pageable) {
        Page<Link> linkList = linkService.listLinks(userId, pageable);

        return null;
    }

    @PutMapping(path = "/{id}/archive", produces = HAL_JSON_VALUE)
    void archiveLink(@PathVariable long linkId) throws ApplicationException {
        linkService.archiveLink(linkId);
    }

    @PutMapping(path = "/{id}/activate", produces = HAL_JSON_VALUE)
    void activateLink(long linkId) {

    }

    @PostMapping(path = "/{id}/tags/{tagName}", produces = HAL_JSON_VALUE)
    void addTag(@PathVariable long linkId, @PathVariable String tagName)
            throws ApplicationException {

        linkService.addTag(linkId, tagName);
    }

    @DeleteMapping(path = "/{id}/tags/{tagName}", produces = HAL_JSON_VALUE)
    void removeTag(long linkId, String tagName) throws ApplicationException {
        linkService.removeTag(linkId, tagName);
    }

}
