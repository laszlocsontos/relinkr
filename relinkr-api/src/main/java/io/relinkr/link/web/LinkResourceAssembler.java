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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import io.relinkr.link.model.LinkStatus;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Factory for creating {@link LinkResource} instances. Leverages
 * {@link IdentifiableResourceAssemblerSupport} for adding self links. Furthermore it also adds
 * links to the shortened link and to possible next link states.
 */
@Slf4j
@Component
public class LinkResourceAssembler
    extends IdentifiableResourceAssemblerSupport<Link, LinkResource> {

  static final String SHORT_LINK_SCHEME = "relinkr.short-link.scheme";
  static final String SHORT_LINK_DOMAIN = "relinkr.short-link.domain";

  private final Environment environment;

  public LinkResourceAssembler(Environment environment) {
    super(LinkResourceController.class, LinkResource.class);
    this.environment = environment;
  }

  @Override
  public LinkResource toResource(Link link) {
    LinkResource linkResource = createResource(link);

    // Add next valid link status links
    link.getUserLinkStatuses().forEach(it -> addUserLinkStatus(linkResource, link.getId(), it));

    // Add short link's link
    addShortLink(linkResource, link.getPath());

    return linkResource;
  }

  @Override
  protected LinkResource instantiateResource(Link link) {
    return new LinkResource(link);
  }

  void addUserLinkStatus(
      LinkResource linkResource, LinkId linkId, LinkStatus linkStatus) {

    linkResource.add(
        linkTo(methodOn(LinkResourceController.class).updateLinkStatus(linkId, linkStatus, null))
            .withRel("userLinkStatuses"));
  }

  void addShortLink(LinkResource linkResource, String path) {
    UriComponentsBuilder uriComponentsBuilder =
        linkTo(methodOn(RedirectController.class).redirectLink(path))
            .toUriComponentsBuilder();

    getShortLinkScheme().ifPresent(uriComponentsBuilder::scheme);

    getShortLinkDomain().ifPresent((domain) -> {
      uriComponentsBuilder.host(domain);
      uriComponentsBuilder.port(-1);
    });

    linkResource.add(
        new org.springframework.hateoas.Link(uriComponentsBuilder.toUriString(),
            "shortLink")
    );
  }

  Optional<String> getShortLinkScheme() {
    return Optional.ofNullable(environment.getProperty(SHORT_LINK_SCHEME));
  }

  Optional<String> getShortLinkDomain() {
    return Optional.ofNullable(environment.getProperty(SHORT_LINK_DOMAIN));
  }

}
