/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package io.relinkr.link.web;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.ResponseEntity.ok;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.model.EmailAddress;
import io.relinkr.link.model.InvalidLinkStatusException;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import io.relinkr.link.model.LinkStatus;
import io.relinkr.link.model.UtmParameters;
import io.relinkr.link.service.LinkService;
import java.security.Principal;
import java.util.EnumSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
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

/**
 * Provides the REST API for managing links.
 */
@RestController
@RequestMapping("/v0/links")
public class LinkResourceController {

  private static Validator FULL_LINK_VALIDATOR = new FullLinkValidator();
  private static Validator PARTIAL_LINK_VALIDATOR = new PartialLinkValidator();

  private final LinkService linkService;
  private final LinkResourceAssembler linkResourceAssembler;

  // FIXME: This could be auto-wired instead of being instantiated here
  private final PagedResourcesAssembler pagedResourcesAssembler =
      new PagedResourcesAssembler(null, null);

  @Autowired
  public LinkResourceController(
      LinkService linkService, LinkResourceAssembler linkResourceAssembler) {

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

    if (EnumSet.of(POST, PUT).contains(httpMethod)) {
      binder.setValidator(FULL_LINK_VALIDATOR);
    } else if (PATCH.equals(httpMethod)) {
      binder.setValidator(PARTIAL_LINK_VALIDATOR);
    }
  }

  @GetMapping(path = "/{linkId}", produces = HAL_JSON_VALUE)
  HttpEntity<LinkResource> getLink(
      @PathVariable LinkId linkId, Principal principal) throws ApplicationException {

    Link link = linkService.getLink(linkId);
    checkAccess(link, principal);

    return ok(linkResourceAssembler.toResource(link));
  }

  @PostMapping(produces = HAL_JSON_VALUE)
  HttpEntity<LinkResource> addLink(
      @Validated @RequestBody LinkResource linkResource,
      Principal principal)
      throws ApplicationException {

    Link link = linkService.addLink(
        linkResource.getLongUrl(),
        linkResource.getUtmParameters().orElse(null),
        getCurrentUser(principal)
    );

    return ok(linkResourceAssembler.toResource(link));
  }

  @PutMapping(path = "/{linkId}", produces = HAL_JSON_VALUE)
  HttpEntity<LinkResource> replaceLink(
      @PathVariable LinkId linkId, @Validated @RequestBody LinkResource linkResource,
      Principal principal)
      throws ApplicationException {

    Link link = linkService.getLink(linkId);
    checkAccess(link, principal);

    String longUrl = linkResource.getLongUrl();
    UtmParameters utmParameters = linkResource.getUtmParameters().orElse(null);

    link = linkService.updateLongUrl(linkId, longUrl, utmParameters);

    return ok(linkResourceAssembler.toResource(link));
  }

  @PatchMapping(path = "/{linkId}", produces = HAL_JSON_VALUE)
  HttpEntity<LinkResource> updateLink(
      @PathVariable LinkId linkId,
      @Validated @RequestBody LinkResource linkResource, Principal principal)
      throws ApplicationException {

    Link link = linkService.getLink(linkId);
    checkAccess(link, principal);

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

  @GetMapping(produces = HAL_JSON_VALUE)
  HttpEntity<PagedResources<LinkResource>> fetchLinks(
      Pageable pageable, Principal principal) {

    Page<Link> linkPage = linkService.fetchLinks(getCurrentUser(principal), pageable);
    return ok(pagedResourcesAssembler.toResource(linkPage, linkResourceAssembler));
  }

  @PutMapping(path = "/{linkId}/linkStatuses/{linkStatus}")
  HttpEntity updateLinkStatus(
      @PathVariable LinkId linkId, @PathVariable LinkStatus linkStatus,
      Principal principal)
      throws ApplicationException {

    Link link = linkService.getLink(linkId);
    checkAccess(link, principal);

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

  @PostMapping(path = "/{linkId}/tags/{tagName}")
  HttpEntity addTag(
      @PathVariable LinkId linkId, @PathVariable String tagName, Principal principal)
      throws ApplicationException {

    Link link = linkService.getLink(linkId);
    checkAccess(link, principal);

    linkService.addTag(linkId, tagName);

    return ok().build();
  }

  @DeleteMapping(path = "/{linkId}/tags/{tagName}")
  HttpEntity removeTag(
      @PathVariable LinkId linkId, @PathVariable String tagName, Principal principal)
      throws ApplicationException {

    Link link = linkService.getLink(linkId);
    checkAccess(link, principal);

    linkService.removeTag(linkId, tagName);

    return ok().build();
  }

  private EmailAddress getCurrentUser(Principal principal) {
    return EmailAddress.of(principal.getName());
  }

  private void checkAccess(Link link, Principal principal) {
    EmailAddress owner = getCurrentUser(principal);
    checkAccess(link, owner);
  }

  private void checkAccess(Link link, EmailAddress owner) {
    if (link.getOwner().equals(owner)) {
      return;
    }

    throw new AccessDeniedException("Access is denied");
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

  private abstract static class AbstractLinkValidator implements Validator {

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
