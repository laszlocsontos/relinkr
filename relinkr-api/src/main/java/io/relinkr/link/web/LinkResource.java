package io.relinkr.link.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.relinkr.core.web.AbstractResource;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkStatus;
import io.relinkr.link.model.Tag;
import io.relinkr.link.model.MissingUtmParameterException;
import io.relinkr.link.model.UtmParameters;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@Relation(value = "link", collectionRelation = "links")
public class LinkResource extends AbstractResource {

    private String longUrl;

    private Set<String> tags;
    private LinkStatus linkStatus;

    @JsonProperty("utmParameters")
    private UtmParametersResource utmParametersResource;
    private String path;
    private String targetUrl;

    public LinkResource(Link link) {
        super(link);

        longUrl = link.getLongUrl().toString();

        tags = link.getTags().stream().map(Tag::getTagName).collect(Collectors.toSet());

        linkStatus = link.getLinkStatus();

        utmParametersResource =
                link.getUtmParameters().map(UtmParametersResource::new).orElse(null);

        path = link.getPath();
        targetUrl = link.getTargetUrl().toString();
    }

    LinkResource(String longUrl) {
        this(longUrl, null);
    }

    LinkResource(UtmParameters utmParameters) {
        this(null, utmParameters);
    }

    LinkResource(String longUrl, UtmParameters utmParameters) {
        this.longUrl = longUrl;
        this.utmParametersResource = Optional.ofNullable(utmParameters)
                .map(UtmParametersResource::new).orElse(null);
    }

    @JsonIgnore
    public Optional<UtmParameters> getUtmParameters() throws MissingUtmParameterException {
        return Optional.ofNullable(utmParametersResource)
                .map(it -> new UtmParameters(it.getUtmSource(), it.getUtmMedium(),
                        it.getUtmCampaign(), it.getUtmTerm(), it.getUtmContent()));
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
