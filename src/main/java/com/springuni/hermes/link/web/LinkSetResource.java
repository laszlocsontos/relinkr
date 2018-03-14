package com.springuni.hermes.link.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springuni.hermes.link.model.LinkSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@Relation(value = "linkSet", collectionRelation = "linkSets")
public class LinkSetResource extends LinkBaseResource {

    @JsonProperty("links")
    private List<LinkResource> linkResources;

    public LinkSetResource(LinkSet linkSet) {
        super(linkSet);
        this.linkResources = linkSet.getEmbeddedLinks().stream().map(LinkResource::new)
                .collect(Collectors.toList());
    }

}
