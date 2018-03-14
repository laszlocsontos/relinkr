package com.springuni.hermes.link.web;

import com.springuni.hermes.link.model.LinkBase;
import com.springuni.hermes.link.model.LinkStatus;
import com.springuni.hermes.link.model.Tag;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

@Getter
@Setter
@NoArgsConstructor
public class LinkBaseResource extends ResourceSupport {

    private String baseUrl;
    private Set<String> tags;
    private LinkStatus linkStatus;

    public LinkBaseResource(LinkBase linkBase) {
        baseUrl = linkBase.getBaseUrl().toString();
        // FIXME: Why?
        tags = (Set<String>)linkBase.getTags().stream().map(it -> ((Tag)it).getTagName()).collect(Collectors.toSet());
        linkStatus = linkBase.getLinkStatus();
    }

    LinkBaseResource(String baseUrl) {
        this.baseUrl = baseUrl;
    }

}
