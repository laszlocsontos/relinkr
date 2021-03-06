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

package io.relinkr.link.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Represents a set of <a href="https://en.wikipedia.org/wiki/UTM_parameters">UTM parameters</a>.
 */
@Embeddable
@EqualsAndHashCode
@ToString
public class UtmParameters implements Serializable {

  private static final long serialVersionUID = 591509236308508122L;

  static final String UTM_SOURCE = "utm_source";
  static final String UTM_MEDIUM = "utm_medium";
  static final String UTM_CAMPAIGN = "utm_campaign";
  static final String UTM_TERM = "utm_term";
  static final String UTM_CONTENT = "utm_content";

  private String utmSource;
  private String utmMedium;
  private String utmCampaign;
  private String utmTerm;
  private String utmContent;

  public UtmParameters(String utmSource, String utmMedium, String utmCampaign)
      throws MissingUtmParameterException {

    this(utmSource, utmMedium, utmCampaign, null, null);
  }

  /**
   * Creates a new UTM parameters instance.
   *
   * @param utmSource UTM source (cannot be empty)
   * @param utmMedium UTM medium (cannot be empty)
   * @param utmCampaign UTM campaign (cannot be empty)
   * @param utmTerm UTM term (can be null or empty)
   * @param utmContent UTM content (can be null or empty)
   * @throws MissingUtmParameterException when UTM parameters are invalid, see above
   */
  public UtmParameters(
      String utmSource, String utmMedium, String utmCampaign, String utmTerm,
      String utmContent) throws MissingUtmParameterException {

    if (!StringUtils.hasText(utmSource)) {
      throw MissingUtmParameterException.forUtmParameter(UTM_SOURCE);
    }

    if (!StringUtils.hasText(utmMedium)) {
      throw MissingUtmParameterException.forUtmParameter(UTM_MEDIUM);
    }

    if (!StringUtils.hasText(utmCampaign)) {
      throw MissingUtmParameterException.forUtmParameter(UTM_CAMPAIGN);
    }

    this.utmSource = utmSource;
    this.utmMedium = utmMedium;
    this.utmCampaign = utmCampaign;
    this.utmTerm = utmTerm;
    this.utmContent = utmContent;
  }

  /*
   * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
   */
  UtmParameters() {
  }

  /**
   * Factory method for creating an {@code UtmParameters} instance from a {@link Map}.
   *
   * @param utmParameterMap Map to convert
   * @return A new {@code UtmParameters} instance
   * @throws MissingUtmParameterException is thrown if the given map contains invalid values
   */
  public static UtmParameters of(Map<String, String> utmParameterMap)
      throws MissingUtmParameterException {

    Assert.notNull(utmParameterMap, "utmParameterMap cannot be null");

    return new UtmParameters(
        utmParameterMap.get(UTM_SOURCE),
        utmParameterMap.get(UTM_MEDIUM),
        utmParameterMap.get(UTM_CAMPAIGN),
        utmParameterMap.get(UTM_TERM),
        utmParameterMap.get(UTM_CONTENT)
    );
  }

  /**
   * Convert this {@code UtmParameters} to a {@link Map}.
   *
   * @return A map based on this instance
   */
  public Map<String, String> asMap() {
    Map<String, String> utmParameterMap = new LinkedHashMap<>();

    // Mandatory UTM parameters
    utmParameterMap.put(UTM_SOURCE, utmSource);
    utmParameterMap.put(UTM_MEDIUM, utmMedium);
    utmParameterMap.put(UTM_CAMPAIGN, utmCampaign);

    // Optional UTM parameters
    getUtmTerm().ifPresent(utmTerm -> utmParameterMap.put(UTM_TERM, utmTerm));
    getUtmContent().ifPresent(utmContent -> utmParameterMap.put(UTM_CONTENT, utmContent));

    return Collections.unmodifiableMap(utmParameterMap);
  }

  public String getUtmSource() {
    return utmSource;
  }

  public String getUtmMedium() {
    return utmMedium;
  }

  public String getUtmCampaign() {
    return utmCampaign;
  }

  public Optional<String> getUtmTerm() {
    return Optional.ofNullable(utmTerm);
  }

  public Optional<String> getUtmContent() {
    return Optional.ofNullable(utmContent);
  }

}
