package com.springuni.hermes.link.web;

import com.springuni.hermes.core.web.AbstractResource;
import com.springuni.hermes.link.model.LinkBase;
import com.springuni.hermes.link.model.LinkStatus;
import com.springuni.hermes.link.model.Tag;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LinkBaseResource extends AbstractResource {

    private String longUrl;
    private Set<String> tags;
    private LinkStatus linkStatus;

    public LinkBaseResource(LinkBase linkBase) {
        longUrl = linkBase.getLongUrl().toString();
        // FIXME: Why?
        tags = (Set<String>) linkBase.getTags().stream().map(it -> ((Tag) it).getTagName())
                .collect(Collectors.toSet());
        linkStatus = linkBase.getLinkStatus();
    }

    LinkBaseResource(String longUrl) {
        this.longUrl = longUrl;
    }

}
