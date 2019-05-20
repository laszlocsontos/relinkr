package io.relinkr.link.web;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.security.authn.annotation.CurrentUser;
import io.relinkr.core.security.authz.annotation.AuthorizeRolesOrOwner;
import io.relinkr.link.model.InvalidLinkStatusException;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import io.relinkr.link.model.LinkStatus;
import io.relinkr.link.model.UtmParameters;
import io.relinkr.link.service.LinkService;
import io.relinkr.user.model.UserId;
import java.util.EnumSet;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

@RestController
@RequestMapping("/api/links")
public class LinkResourceController {

    private static Validator FULL_LINK_VALIDATOR = new FullLinkValidator();
    private static Validator PARTIAL_LINK_VALIDATOR = new PartialLinkValidator();

    private final LinkService linkService;
    private final LinkResourceAssembler linkResourceAssembler;
    private final PagedResourcesAssembler pagedResourcesAssembler =
            new PagedResourcesAssembler(null, null);

    public LinkResourceController(LinkService linkService,
            LinkResourceAssembler linkResourceAssembler) {
        this.linkService = linkService;
        this.linkResourceAssembler = linkResourceAssembler;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder, ServletWebRequest request) {
        Object target = binder.getTarget();
        if (target == null || !LinkResource.class.isAssignableFrom(target.getClass())) {
            return;
        }

        HttpMethod httpMethod = request.getHttpMethod();
        switch (httpMethod) {
            case POST:
            case PUT:
                binder.setValidator(FULL_LINK_VALIDATOR);
                break;
            case PATCH:
                binder.setValidator(PARTIAL_LINK_VALIDATOR);
                break;
        }
    }

    @AuthorizeRolesOrOwner
    @GetMapping(path = "/{linkId}", produces = HAL_JSON_VALUE)
    HttpEntity<LinkResource> getLink(@PathVariable LinkId linkId) throws ApplicationException {
        Link link = linkService.getLink(linkId);
        return ok(linkResourceAssembler.toResource(link));
    }

    @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
    @PostMapping(produces = HAL_JSON_VALUE)
    HttpEntity<LinkResource> addLink(@CurrentUser UserId userId,
            @Validated @RequestBody LinkResource linkResource)
            throws ApplicationException {

        Link link = linkService.addLink(
                linkResource.getLongUrl(),
                linkResource.getUtmParameters().orElse(null),
                userId
        );

        return ok(linkResourceAssembler.toResource(link));
    }

    @AuthorizeRolesOrOwner
    @PutMapping(path = "/{linkId}", produces = HAL_JSON_VALUE)
    HttpEntity<LinkResource> replaceLink(
            @PathVariable LinkId linkId, @Validated @RequestBody LinkResource linkResource)
            throws ApplicationException {

        String longUrl = linkResource.getLongUrl();
        UtmParameters utmParameters = linkResource.getUtmParameters().orElse(null);

        Link link = linkService.updateLongUrl(linkId, longUrl, utmParameters);

        return ok(linkResourceAssembler.toResource(link));
    }

    @AuthorizeRolesOrOwner
    @PatchMapping(path = "/{linkId}", produces = HAL_JSON_VALUE)
    HttpEntity<LinkResource> updateLink(
            @PathVariable LinkId linkId,
            @Validated @RequestBody LinkResource linkResource)
            throws ApplicationException {

        Link link = null;
        String longUrl = linkResource.getLongUrl();
        Optional<UtmParameters> utmParameters = linkResource.getUtmParameters();

        if (!StringUtils.isEmpty(longUrl)) {
            link = linkService.updateLongUrl(linkId, longUrl);
        }

        if (utmParameters.isPresent()) {
            link = linkService.updateUtmParameters(linkId, utmParameters.get());
        }

        return ok(linkResourceAssembler.toResource(link));
    }

    @AuthorizeRolesOrOwner(roles = {"ROLE_USER"})
    @GetMapping(produces = HAL_JSON_VALUE)
    HttpEntity<PagedResources<LinkResource>> listLinks(
            @CurrentUser UserId userId, Pageable pageable) {

        Page<Link> linkPage = linkService.listLinks(userId, pageable);
        return ok(pagedResourcesAssembler.toResource(linkPage, linkResourceAssembler));
    }

    @AuthorizeRolesOrOwner
    @PutMapping(path = "/{linkId}/linkStatuses/{linkStatus}")
    HttpEntity updateLinkStatus(@PathVariable LinkId linkId, @PathVariable LinkStatus linkStatus)
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
                        linkStatus, EnumSet.of(LinkStatus.ACTIVE, LinkStatus.ARCHIVED)
                );
        }

        return ok().build();
    }

    @AuthorizeRolesOrOwner
    @PostMapping(path = "/{linkId}/tags/{tagName}")
    HttpEntity addTag(@PathVariable LinkId linkId, @PathVariable String tagName)
            throws ApplicationException {

        linkService.addTag(linkId, tagName);

        return ok().build();
    }

    @AuthorizeRolesOrOwner
    @DeleteMapping(path = "/{linkId}/tags/{tagName}")
    HttpEntity removeTag(@PathVariable LinkId linkId, @PathVariable String tagName)
            throws ApplicationException {
        linkService.removeTag(linkId, tagName);

        return ok().build();
    }

    static class FullLinkValidator extends AbstractLinkValidator {

        @Override
        void doValidate(LinkResource linkResource, Errors errors) {
            if (!containsLongUrl(linkResource)) {
                errors.rejectValue("longUrl", "links.long-url-is-missing-or-empty");
            }
        }

    }

    static class PartialLinkValidator extends AbstractLinkValidator {

        @Override
        void doValidate(LinkResource linkResource, Errors errors) {
            if (!containsLongUrl(linkResource) && !containsUtmParameters(linkResource)) {
                errors.reject("links.either-long-url-or-utm-parameters-have-to-be-specified");
            }
        }

    }

    private static abstract class AbstractLinkValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return LinkResource.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            if (target == null) {
                errors.reject("general.request-body-is-empty");
                return;
            }

            doValidate((LinkResource) target, errors);
        }

        abstract void doValidate(LinkResource linkResource, Errors errors);

        boolean containsLongUrl(LinkResource linkResource) {
            return !StringUtils.isEmpty(linkResource.getLongUrl());
        }

        boolean containsUtmParameters(LinkResource linkResource) {
            return linkResource.getUtmParametersResource() != null;
        }

    }

}