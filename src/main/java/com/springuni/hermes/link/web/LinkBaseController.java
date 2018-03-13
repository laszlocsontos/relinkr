package com.springuni.hermes.link.web;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

import com.springuni.hermes.core.EntityNotFoundException;
import com.springuni.hermes.link.model.LinkBase;
import com.springuni.hermes.link.service.LinkBaseService;
import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class LinkBaseController<
        ID extends Serializable,
        L extends LinkBase<ID>,
        S extends LinkBaseService<ID, L>,
        R extends LinkBaseResource
        > {

    protected final S linkService;

    public LinkBaseController(S linkService) {
        this.linkService = linkService;
    }

    @GetMapping(path = "/{id}", produces = HAL_JSON_VALUE)
    R getLink(@PathVariable ID linkId) {
        return null;
    }

    @GetMapping(path = "/", produces = HAL_JSON_VALUE)
    Page<R> listLinks(@RequestParam(required = false) long userId, Pageable pageable) {
        Page<L> linkList = linkService.listLinks(userId, pageable);

        return null;
    }

    @PutMapping(path = "/{id}/archive", produces = HAL_JSON_VALUE)
    void archiveLink(ID linkId) {
    }

    @PutMapping(path = "/{id}/activate", produces = HAL_JSON_VALUE)
    void activateLink(ID linkId) {
    }

    @PostMapping(path = "/{id}/tags/{tagName}", produces = HAL_JSON_VALUE)
    void addTag(@PathVariable ID linkId, @PathVariable String tagName) {

    }

    @DeleteMapping(path = "/{id}/tags/{tagName}", produces = HAL_JSON_VALUE)
    void removeTag(ID linkId, String tagName) throws EntityNotFoundException {

    }

}
