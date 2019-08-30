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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.relinkr.core.web.AbstractResource;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkStatus;
import io.relinkr.link.model.MissingUtmParameterException;
import io.relinkr.link.model.Tag;
import io.relinkr.link.model.UtmParameters;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.core.Relation;

/**
 * DTO which represents a {@link Link} as a REST resource.
 */
@Getter
@Setter
@NoArgsConstructor
@Relation(value = "link", collectionRelation = "links")
class LinkResource extends AbstractResource {

  private String longUrl;

  private Set<String> tags;
  private LinkStatus linkStatus;

  @JsonProperty("utmParameters")
  private UtmParametersResource utmParametersResource;
  private String path;
  private String targetUrl;

  /**
   * Creates a new DTO representing a {@link Link}.
   *
   * @param link that {@code Link} this DTO will represent
   */
  LinkResource(Link link) {
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
  Optional<UtmParameters> getUtmParameters() throws MissingUtmParameterException {
    return Optional.ofNullable(utmParametersResource)
        .map(it -> new UtmParameters(it.getUtmSource(), it.getUtmMedium(),
            it.getUtmCampaign(), it.getUtmTerm(), it.getUtmContent()));
  }

  @Data
  @NoArgsConstructor
  static class UtmParametersResource {

    private String utmSource;
    private String utmMedium;
    private String utmCampaign;
    private String utmTerm;
    private String utmContent;

    UtmParametersResource(UtmParameters utmParameters) {
      utmSource = utmParameters.getUtmSource();
      utmMedium = utmParameters.getUtmMedium();
      utmCampaign = utmParameters.getUtmCampaign();
      utmParameters.getUtmContent().ifPresent(this::setUtmContent);
      utmParameters.getUtmTerm().ifPresent(this::setUtmTerm);
    }

  }

}
