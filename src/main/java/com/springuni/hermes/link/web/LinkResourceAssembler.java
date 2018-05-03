package com.springuni.hermes.link.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.link.model.LinkStatus;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class LinkResourceAssembler
        extends IdentifiableResourceAssemblerSupport<Link, LinkResource> {

    static final String SHORT_LINK_SCHEME = "relinkr.shortLink.scheme";
    static final String SHORT_LINK_DOMAIN = "relinkr.shortLink.domain";

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
                linkTo(methodOn(LinkResourceController.class).updateLinkStatus(linkId, linkStatus))
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

    private Optional<String> getShortLinkScheme() {
        return Optional.ofNullable(environment.getProperty(SHORT_LINK_SCHEME));
    }

    private Optional<String> getShortLinkDomain() {
        return Optional.ofNullable(environment.getProperty(SHORT_LINK_DOMAIN));
    }

}
