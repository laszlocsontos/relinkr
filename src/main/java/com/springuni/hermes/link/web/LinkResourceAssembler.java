package com.springuni.hermes.link.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LinkResourceAssembler
        extends IdentifiableResourceAssemblerSupport<Link, LinkResource> {

    public LinkResourceAssembler() {
        super(LinkResourceController.class, LinkResource.class);
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

    private void addUserLinkStatus(LinkResource linkResource, Long linkId, LinkStatus linkStatus) {
        linkResource.add(
                linkTo(methodOn(LinkResourceController.class).updateLinkStatus(linkId, linkStatus))
                        .withRel("userLinkStatuses"));
    }

    private void addShortLink(LinkResource linkResource, String path) {
        linkResource.add(linkTo(methodOn(RedirectController.class).redirectLink(path))
                .withRel("shortLink"));
    }

}
