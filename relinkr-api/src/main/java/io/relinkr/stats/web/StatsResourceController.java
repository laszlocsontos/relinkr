package io.relinkr.stats.web;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.security.authn.annotation.CurrentUser;
import io.relinkr.core.security.authz.annotation.AuthorizeRolesOrOwner;
import io.relinkr.stats.model.StatEntry;
import io.relinkr.stats.model.StatType;
import io.relinkr.stats.model.Stats;
import io.relinkr.stats.model.TimeSpan;
import io.relinkr.user.model.UserId;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Provides the REST API for retrieving statistics
 */
@RestController
@RequestMapping("/v1/stats")
public class StatsResourceController {

    private final StatsResourceAssembler<String> statsAssembler = new StatsResourceAssembler<>();

    @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
    @GetMapping(path = "/links", produces = HAL_JSON_VALUE)
    // TODO: Add path variable: requested timespan
    public HttpEntity<StatsResource> getLinksStats(@CurrentUser UserId userId)
            throws ApplicationException {

        List<StatEntry<String>> entries = Arrays.asList(
            StatEntry.of("2018-03-06", 5),
            StatEntry.of("2018-03-07", 2));

        List<TimeSpan<String>> timeSpans = Arrays.asList(
            TimeSpan.of("today","2019-06-03", "2019-06-03"),
            TimeSpan.of("yesterday","2019-06-02", "2019-06-02"),
            TimeSpan.of("last7days","2019-05-27", "2019-06-02")
        );

        TimeSpan<String> ts = TimeSpan.of("custom", "2018-03-06", "2018-03-12");
        Stats<String> stats = Stats.of(StatType.LINKS, entries, ts, timeSpans);

        StatsResource resource = statsAssembler.toResource(stats);
        return ok(resource);
    }

    @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
    @GetMapping(path = "/clicks", produces = HAL_JSON_VALUE)
    public HttpEntity<StatsResource> getClicksStats(@CurrentUser UserId userId)
            throws ApplicationException {
        throw new NotImplementedException();
    }

    @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
    @GetMapping(path = "/visitors", produces = HAL_JSON_VALUE)
    public HttpEntity<StatsResource> getVisitorsStats(@CurrentUser UserId userId)
            throws ApplicationException {
        throw new NotImplementedException();
    }
}
