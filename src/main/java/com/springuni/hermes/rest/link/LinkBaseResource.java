package com.springuni.hermes.rest.link;

import com.springuni.hermes.domain.link.LinkBase;
import com.springuni.hermes.domain.link.LinkStatus;
import com.springuni.hermes.domain.link.Tag;
import java.io.Serializable;
import java.net.URL;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

@Getter
@Setter
@NoArgsConstructor
public class LinkBaseResource extends ResourceSupport {

    private String baseUrl;
    private Set<Tag> tags;
    private LinkStatus linkStatus;

    public LinkBaseResource(LinkBase linkBase) {
        baseUrl = linkBase.getBaseUrl().toString();
        tags = linkBase.getTags();
        linkStatus = linkBase.getLinkStatus();
    }

    LinkBaseResource(String baseUrl) {
        this.baseUrl = baseUrl;
    }

}
