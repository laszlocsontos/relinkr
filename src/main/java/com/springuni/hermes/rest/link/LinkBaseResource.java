package com.springuni.hermes.rest.link;

import com.springuni.hermes.domain.link.LinkBase;
import com.springuni.hermes.domain.link.LinkStatus;
import com.springuni.hermes.domain.link.Tag;
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

    private Long userId;
    private URL baseUrl;
    private Set<Tag> tags;
    private LinkStatus linkStatus;

    public LinkBaseResource(LinkBase linkBase) {
        userId = linkBase.getUserId();
        baseUrl = linkBase.getBaseUrl();
        tags = linkBase.getTags();
        linkStatus = linkBase.getLinkStatus();
    }

}
