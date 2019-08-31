/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package io.relinkr.link.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.relinkr.core.web.EntityResource;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkStatus;
import io.relinkr.link.model.MissingUtmParameterException;
import io.relinkr.link.model.Tag;
import io.relinkr.link.model.UtmParameters;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * DTO which represents a {@link Link} as a REST resource.
 */
@Getter
@Setter
@Relation(value = "link", collectionRelation = "links")
@SuppressFBWarnings(
    value = {"EQ_DOESNT_OVERRIDE_EQUALS"},
    justification = "Overriding equals() wouldn't contribute to the class' identity"
)
class LinkResource extends ResourceSupport {

  @JsonUnwrapped
  private final EntityResource entityResource;

  private final String longUrl;

  private final Set<String> tags;
  private final LinkStatus linkStatus;

  @JsonProperty("utmParameters")
  private final UtmParametersResource utmParametersResource;
  private final String path;
  private final String targetUrl;

  LinkResource(
      EntityResource entityResource, String longUrl,
      Set<String> tags, LinkStatus linkStatus,
      UtmParametersResource utmParametersResource, String path, String targetUrl) {

    this.entityResource = entityResource;
    this.longUrl = longUrl;
    this.tags = tags;
    this.linkStatus = linkStatus;
    this.utmParametersResource = utmParametersResource;
    this.path = path;
    this.targetUrl = targetUrl;
  }

  @JsonCreator
  LinkResource(
      String resourceId,
      LocalDateTime createdDate, LocalDateTime lastModifiedDate,
      Integer version,
      String longUrl, Set<String> tags, LinkStatus linkStatus,
      @JsonProperty("utmParameters") UtmParametersResource utmParametersResource,
      String path, String targetUrl) {

    this.entityResource = EntityResource.of(resourceId, createdDate, lastModifiedDate, version);
    this.longUrl = longUrl;
    this.tags = tags;
    this.linkStatus = linkStatus;
    this.utmParametersResource = utmParametersResource;
    this.path = path;
    this.targetUrl = targetUrl;
  }

  /**
   * Creates a new DTO representing a {@link Link}.
   *
   * @param link that {@code Link} this DTO will represent
   */
  static LinkResource of(Link link) {
    String longUrl = link.getLongUrl().toString();

    Set<String> tags = link.getTags().stream().map(Tag::getTagName).collect(Collectors.toSet());

    LinkStatus linkStatus = link.getLinkStatus();

    UtmParametersResource utmParametersResource =
        link.getUtmParameters().map(UtmParametersResource::of).orElse(null);

    String path = link.getPath();
    String targetUrl = link.getTargetUrl().toString();

    return new LinkResource(
        EntityResource.of(link),
        longUrl, tags, linkStatus, utmParametersResource, path, targetUrl
    );
  }

  static LinkResource of(String longUrl) {
    return of(longUrl, null);
  }

  static LinkResource of(UtmParameters utmParameters) {
    return of(null, utmParameters);
  }

  static LinkResource of(String longUrl, UtmParameters utmParameters) {
    UtmParametersResource utmParametersResource = Optional.ofNullable(utmParameters)
        .map(UtmParametersResource::of).orElse(null);

    return new LinkResource(
        null, longUrl, Collections.emptySet(), null, utmParametersResource, null, null
    );
  }

  @JsonIgnore
  Optional<UtmParameters> getUtmParameters() throws MissingUtmParameterException {
    return Optional.ofNullable(utmParametersResource)
        .map(it -> new UtmParameters(it.getUtmSource(), it.getUtmMedium(),
            it.getUtmCampaign(), it.getUtmTerm(), it.getUtmContent()));
  }

  @Data
  static class UtmParametersResource {

    private final String utmSource;
    private final String utmMedium;
    private final String utmCampaign;
    private final String utmTerm;
    private final String utmContent;

    @JsonCreator
    UtmParametersResource(
        String utmSource, String utmMedium, String utmCampaign, String utmTerm, String utmContent) {

      this.utmSource = utmSource;
      this.utmMedium = utmMedium;
      this.utmCampaign = utmCampaign;
      this.utmTerm = utmTerm;
      this.utmContent = utmContent;
    }

    static UtmParametersResource of(UtmParameters utmParameters) {
      String utmSource = utmParameters.getUtmSource();
      String utmMedium = utmParameters.getUtmMedium();
      String utmCampaign = utmParameters.getUtmCampaign();
      String utmContent = utmParameters.getUtmContent().orElse(null);
      String utmTerm = utmParameters.getUtmTerm().orElse(null);

      return new UtmParametersResource(utmSource, utmMedium, utmCampaign, utmTerm, utmContent);
    }

  }

}
