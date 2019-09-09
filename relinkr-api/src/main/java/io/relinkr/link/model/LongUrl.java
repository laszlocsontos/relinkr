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

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.stream.Collectors.toMap;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Serializable;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Represents an original, so called long URL, or in other words that one that will be shortened.
 * It consists of a base URL and a set of UTM parameters, if they present.
 */
@Embeddable
@EqualsAndHashCode
public class LongUrl implements Serializable {

  private static final long serialVersionUID = -6894255399882709562L;

  // https://gist.github.com/dperini/729294
  private static final Pattern HTTP_URL_PATTERN = Pattern.compile(
      "^"
          // protocol identifier
          + "(?:(?:https?)://)"
          // user:pass authentication
          + "(?:\\S+(?::\\S*)?@)?"
          + "(?:"
          // IP address exclusion
          // private & local networks
          + "(?!(?:10|127)(?:\\.\\d{1,3}){3})"
          + "(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})"
          + "(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})"
          // IP address dotted notation octets
          // excludes loopback network 0.0.0.0
          // excludes reserved space >= 224.0.0.0
          // excludes network & broacast addresses
          // (first & last IP address of each class)
          + "(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])"
          + "(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}"
          + "(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))"
          + "|"
          // host name
          + "(?:(?:[a-z¡-\\uffff0-9]-*)*[a-z¡-\\uffff0-9]+)"
          // domain name
          + "(?:\\.(?:[a-z¡-\\uffff0-9]-*)*[a-z¡-\\uffff0-9]+)*"
          // TLD identifier
          + "(?:\\.(?:[a-z¡-\\uffff]{2,}))"
          // TLD may end with dot
          + "\\.?"
          + ")"
          // port number
          + "(?::\\d{2,5})?"
          // resource path
          + "(?:[/?#]\\S*)?"
          + "$",
      CASE_INSENSITIVE
  );

  private URI longUrl;

  @Transient
  private URI targetUrl;

  @Embedded
  private UtmParameters utmParameters;

  /*
   * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
   */
  LongUrl() {
  }

  private LongUrl(URI longUrl, URI targetUrl, UtmParameters utmParameters) {
    this.longUrl = longUrl;
    this.targetUrl = targetUrl;
    this.utmParameters = utmParameters;
  }

  /**
   * Factory method for creating a new {@code LongUrl} instance with the following parameters.
   *
   * @param url Original (long) URL, cannot be {@code null}
   * @throws InvalidUrlException is thrown in case of an invalid URL is given
   */
  public static LongUrl from(String url) throws InvalidUrlException {
    return from(url, null);
  }

  /**
   * Factory method for creating a new {@code LongUrl} instance with the following parameters
   *
   * @param url Original (long) URL, cannot be {@code null}
   * @param initialUtmParameters UTM parameters (optional, ie. can be {@code null})
   * @throws InvalidUrlException is thrown in case of an invalid URL is given
   */
  @SuppressFBWarnings(
      value = {"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"},
      justification = "NPE is not possible in UriComponentsBuilder.path()"
  )
  public static LongUrl from(@NonNull String url, UtmParameters initialUtmParameters) {
    UriComponents uriComponents = parseUrl(url);

    MultiValueMap<String, String> queryParams =
        new LinkedMultiValueMap<>(uriComponents.getQueryParams());

    final UtmParameters utmParameters =
        createUtmParameters(initialUtmParameters, queryParams.toSingleValueMap());

    queryParams.remove(UtmParameters.UTM_SOURCE);
    queryParams.remove(UtmParameters.UTM_MEDIUM);
    queryParams.remove(UtmParameters.UTM_CAMPAIGN);
    queryParams.remove(UtmParameters.UTM_TERM);
    queryParams.remove(UtmParameters.UTM_CONTENT);

    URI longUrl = UriComponentsBuilder
        .newInstance()
        .scheme(uriComponents.getScheme())
        .userInfo(uriComponents.getUserInfo())
        .host(uriComponents.getHost())
        .port(uriComponents.getPort())
        .path(uriComponents.getPath())
        .queryParams(queryParams)
        .fragment(uriComponents.getFragment())
        .build()
        .toUri();

    return new LongUrl(longUrl, createTargetUrl(longUrl, utmParameters), utmParameters);
  }

  /**
   * Factory method for creating a new {@code LongUrl} instance with the following parameters
   *
   * @param uri Original (long) URL, cannot be {@code null}
   * @param utmParameters UTM parameters (optional, ie. can be {@code null})
   * @throws InvalidUrlException is thrown in case of an invalid URL is given
   */
  public static LongUrl from(@NonNull URI uri, UtmParameters utmParameters) {
    try {
      return LongUrl.from(uri.toString(), utmParameters);
    } catch (InvalidUrlException iue) {
      // This should never happen as url itself is a valid java.net.URL.
      throw new AssertionError("Internal error: longUrl=" + uri, iue);
    }
  }

  static UriComponents parseUrl(String url) throws InvalidUrlException {
    if (StringUtils.isEmpty(url)) {
      throw new InvalidUrlException("Invalid URL: empty");
    }

    // This workaround is required as UriComponentsBuilder.fromHttpUrl() doesn't accept URLs
    // with fragments.
    Matcher matcher = HTTP_URL_PATTERN.matcher(url);
    if (!matcher.matches()) {
      throw new InvalidUrlException("Invalid URL: " + url);
    }

    try {
      return UriComponentsBuilder.fromUriString(url).build();
    } catch (IllegalArgumentException iae) {
      throw new InvalidUrlException(iae);
    }
  }

  /**
   * Applies the given {@code utmParameters} to this {@code LongUrl}.
   *
   * @param utmParameters UTM parameters to apply
   * @return A new {@code LongUrl}
   */
  public LongUrl apply(UtmParameters utmParameters) {
    return from(longUrl, utmParameters);
  }

  public URI getLongUrl() {
    return longUrl;
  }

  /**
   * Returns the target URL this {@code LongUrl} represents, that is, it will contain all the UTM
   * parameters.
   *
   * @return the full target URL
   */
  public URI getTargetUrl() {
    return targetUrl;
  }

  public Optional<UtmParameters> getUtmParameters() {
    return Optional.ofNullable(utmParameters);
  }

  public boolean hasUtmParameters() {
    return (utmParameters != null);
  }

  @Override
  public String toString() {
    return getTargetUrl().toString();
  }

  private static URI createTargetUrl(URI longUrl, UtmParameters utmParameters) {
    Assert.notNull(longUrl, "longUrl cannot be null");

    UriComponentsBuilder targetUrlBuilder = UriComponentsBuilder.fromUri(longUrl);

    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

    Optional.ofNullable(utmParameters)
        .map(UtmParameters::asMap)
        .map(it -> it.entrySet()
            .stream()
            .filter(e -> StringUtils.hasText(e.getValue()))
            .collect(toMap(Entry::getKey, Entry::getValue, (ov, nv) -> nv, LinkedHashMap::new))
        )
        .ifPresent(queryParams::setAll);

    targetUrlBuilder.queryParams(queryParams);

    return targetUrlBuilder.build().toUri();
  }

  private static UtmParameters createUtmParameters(
      UtmParameters initialUtmParameters, Map<String, String> utmParameterMap) {

    if (initialUtmParameters != null) {
      return initialUtmParameters;
    }

    try {
      return UtmParameters.of(utmParameterMap);
    } catch (MissingUtmParameterException mupe) {
      return null;
    }
  }

  /**
   * The targetUrl is not persisted to the database and hence when loading {@code LongUrl} the field will remain null.
   * Because of this we need to calculate it after it has been loaded.
   */
  @PostLoad
  private void postLoad() {
    this.targetUrl = createTargetUrl(this.longUrl, this.utmParameters);
  }

}
