package io.relinkr.link.model;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Embeddable
@EqualsAndHashCode
public class LongUrl {

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

  public LongUrl(String url) throws InvalidUrlException {
    this(url, null);
  }

  /*
   * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
   */
  LongUrl() {
  }

  LongUrl(@NonNull String url, UtmParameters utmParameters) throws InvalidUrlException {
    UriComponents uriComponents = parseUrl(url);

    MultiValueMap<String, String> queryParams =
        new LinkedMultiValueMap<>(uriComponents.getQueryParams());

    this.utmParameters = createUtmParameters(utmParameters, queryParams.toSingleValueMap());

    queryParams.remove(UtmParameters.UTM_SOURCE);
    queryParams.remove(UtmParameters.UTM_MEDIUM);
    queryParams.remove(UtmParameters.UTM_CAMPAIGN);
    queryParams.remove(UtmParameters.UTM_TERM);
    queryParams.remove(UtmParameters.UTM_CONTENT);

    longUrl = UriComponentsBuilder
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
  }

  /**
   * Factory method for creating a new {@code LongUrl} instance with the following parameters
   *
   * @param uri Original (long) URL, cannot be {@code null}
   * @param utmParameters UTM parameters (optional, ie. can be {@code null}
   * @throws InvalidUrlException is thrown in case of an invalid URL is given
   */
  public static LongUrl from(@NonNull URI uri, UtmParameters utmParameters) {
    try {
      return new LongUrl(uri.toString(), utmParameters);
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
    if (targetUrl == null) {
      targetUrl = createTargetUrl();
    }

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

  private URI createTargetUrl() {
    Assert.notNull(longUrl, "longUrl cannot be null");

    UriComponentsBuilder targetUrlBuilder = UriComponentsBuilder.fromUri(longUrl);

    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

    Optional.ofNullable(utmParameters)
        .map(UtmParameters::asMap)
        .ifPresent(queryParams::setAll);

    targetUrlBuilder.queryParams(queryParams);

    return targetUrlBuilder.build().toUri();
  }

  private UtmParameters createUtmParameters(
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

}
