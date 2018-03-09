package com.springuni.hermes.rest.link;

import com.springuni.hermes.domain.link.Link;
import com.springuni.hermes.domain.utm.UtmParameters;
import java.net.URL;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LinkResource extends LinkBaseResource {

    private UtmParameters utmParameters;
    private String path;
    private URL targetUrl;

    public LinkResource(Link link) {
        super(link);
        utmParameters = null; // TODO
        path = link.getPath();
        targetUrl = link.getTargetUrl();
    }

}
