package io.relinkr.stats.web;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.security.authn.annotation.CurrentUser;
import io.relinkr.core.security.authz.annotation.AuthorizeRolesOrOwner;
import io.relinkr.stats.model.StatEntry;
import io.relinkr.user.model.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Provides the REST API for retrieving statistics
 */
@RestController
@RequestMapping("/v1/stats")
public class StatsResourceController {

    private final PagedResourcesAssembler pageAssembler = new PagedResourcesAssembler(null, null);
    private final StatEntryResourceAssembler entryAssembler = new StatEntryResourceAssembler();

    @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
    @GetMapping(path = "/links", produces = HAL_JSON_VALUE)
    // TODO: Add path variable: requested timespan
    HttpEntity<PagedResources<StatEntry>> getLinksStats(@CurrentUser UserId userId)
            throws ApplicationException {

        // TODO: replace fake data by query results
        List<StatEntry> stats = new ArrayList<>();
        stats.add(StatEntry.of("2018-03-06", 5));
        stats.add(StatEntry.of("2018-03-07", 2));

        // TODO: Implement Pageable and pass it to PageImpl constructor
        // TODO: then pageAssembler would create the pagination links as well I assume
        Page<StatEntry> page = new PageImpl<>(stats);
        PagedResources<StatEntry> resources = pageAssembler.toResource(page, entryAssembler);
        // TODO: add "timespan" property to the returned Resource
        return ok(resources);
    }
}
