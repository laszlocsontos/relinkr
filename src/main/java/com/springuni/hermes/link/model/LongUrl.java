package com.springuni.hermes.link.model;

import static com.springuni.hermes.utm.model.UtmParameters.UTM_CAMPAIGN;
import static com.springuni.hermes.utm.model.UtmParameters.UTM_CONTENT;
import static com.springuni.hermes.utm.model.UtmParameters.UTM_MEDIUM;
import static com.springuni.hermes.utm.model.UtmParameters.UTM_SOURCE;
import static com.springuni.hermes.utm.model.UtmParameters.UTM_TERM;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

import com.springuni.hermes.utm.model.MissingUtmParameterException;
import com.springuni.hermes.utm.model.UtmParameters;
import io.jsonwebtoken.lang.Assert;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Embeddable
@EqualsAndHashCode
public class LongUrl {

    private static final Pattern HTTP_URL_PATTERN = Pattern.compile(
            "^(https?:\\/\\/)?" + // protocol
                    "((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.?)+[a-z]{2,}|" + // domain name
                    "((\\d{1,3}\\.){3}\\d{1,3}))" + // OR ip (v4) address
                    "(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*" + // port and path
                    "(\\?[;&a-z\\d%_.~+=-]*)?" + // query string
                    "(\\#[-a-z\\d_]*)?$",
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

    public LongUrl(String url, UtmParameters utmParameters) throws InvalidUrlException {
        UriComponents uriComponents = parseUrl(url);

        MultiValueMap<String, String> queryParams =
                new LinkedMultiValueMap<>(uriComponents.getQueryParams());

        this.utmParameters = createUtmParameters(utmParameters, queryParams.toSingleValueMap());

        queryParams.remove(UTM_SOURCE);
        queryParams.remove(UTM_MEDIUM);
        queryParams.remove(UTM_CAMPAIGN);
        queryParams.remove(UTM_TERM);
        queryParams.remove(UTM_CONTENT);

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

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    LongUrl() {
    }

    public static LongUrl from(URI uri, UtmParameters utmParameters) {
        try {
            return new LongUrl(uri.toString(), utmParameters);
        } catch (InvalidUrlException e) {
            // This should never happen as url itself is a valid java.net.URL.
            throw new AssertionError("Internal error: longUrl=" + uri, e);
        }
    }

    public LongUrl apply(UtmParameters utmParameters) {
        return from(longUrl, utmParameters);
    }

    public URI getLongUrl() {
        return longUrl;
    }

    public URI getTargetUrl() {
        if (targetUrl == null) {
            targetUrl = createTargetUrl();
        }

        return targetUrl;
    }

    public UtmParameters getUtmParameters() {
        return utmParameters;
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
        } catch (MissingUtmParameterException e) {
            return null;
        }
    }

    private UriComponents parseUrl(String url) throws InvalidUrlException {
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
        } catch (IllegalArgumentException e) {
            throw new InvalidUrlException(e);
        }
    }

}
