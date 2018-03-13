package com.springuni.hermes.rest.link;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springuni.hermes.link.Link;
import com.springuni.hermes.link.LinkBase;
import com.springuni.hermes.utm.UtmParameters;
import java.util.Optional;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@Relation(value = "link", collectionRelation = "links")
public class LinkResource extends LinkBaseResource {

    @JsonProperty("utmParameters")
    private UtmParametersResource utmParametersResource;
    private String path;
    private String targetUrl;

    public LinkResource(Link link) {
        super(link);
        utmParametersResource = new UtmParametersResource(link.getUtmParameters());
        path = link.getPath();
        targetUrl = link.getTargetUrl().toString();
    }

    LinkResource(String baseUrl, UtmParameters utmParameters) {
        super(baseUrl);
        this.utmParametersResource = new UtmParametersResource(utmParameters);
    }

    @JsonIgnore
    public UtmParameters getUtmParameters() {
        return Optional.ofNullable(utmParametersResource)
                .map(it -> new UtmParameters(it.getUtmSource(), it.getUtmMedium(),
                        it.getUtmCampaign(), it.getUtmTerm(), it.getUtmContent())).orElse(null);
    }

    @Data
    @NoArgsConstructor
    public class UtmParametersResource {

        private String utmSource;
        private String utmMedium;
        private String utmCampaign;
        private String utmTerm;
        private String utmContent;

        public UtmParametersResource(UtmParameters utmParameters) {
            utmSource = utmParameters.getUtmSource();
            utmMedium = utmParameters.getUtmMedium();
            utmCampaign = utmParameters.getUtmCampaign();
            utmParameters.getUtmContent().ifPresent(this::setUtmContent);
            utmParameters.getUtmTerm().ifPresent(this::setUtmTerm);
        }

    }

}
