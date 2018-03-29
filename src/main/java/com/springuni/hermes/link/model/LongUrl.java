package com.springuni.hermes.link.model;

import static com.springuni.hermes.utm.model.UtmParameters.UTM_CAMPAIGN;
import static com.springuni.hermes.utm.model.UtmParameters.UTM_CONTENT;
import static com.springuni.hermes.utm.model.UtmParameters.UTM_MEDIUM;
import static com.springuni.hermes.utm.model.UtmParameters.UTM_SOURCE;
import static com.springuni.hermes.utm.model.UtmParameters.UTM_TERM;

import com.springuni.hermes.utm.model.MissingUtmParameterException;
import com.springuni.hermes.utm.model.UtmParameters;
import java.net.URI;
import java.net.URISyntaxException;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.EqualsAndHashCode;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Embeddable
@EqualsAndHashCode
public class LongUrl {

    private URI longUrl;

    // @Transient
    private URI targetUrl;

    @Embedded
    private UtmParameters utmParameters;

    public LongUrl(String uriString) throws InvalidUrlException {
        this(uriString, null);
    }

    public LongUrl(String uriString, UtmParameters utmParameters) throws InvalidUrlException {
        if (uriString == null) {
            throw new InvalidUrlException("URI is null");
        }

        URI uri;
        try {
            uri = new URI(uriString);
        } catch (URISyntaxException e) {
            throw new InvalidUrlException(e);
        }

        String scheme = uri.getScheme();
        if (scheme == null || !StringUtils.startsWithIgnoreCase(scheme, "http")) {
            throw new InvalidUrlException("Invalid scheme: " + scheme);
        }

        UriComponents uriComponents = UriComponentsBuilder.fromUri(uri).build();

        MultiValueMap<String, String> queryParams =
                new LinkedMultiValueMap<>(uriComponents.getQueryParams());

        if (utmParameters != null) {
            this.utmParameters = utmParameters;
        } else {
            try {
                this.utmParameters = UtmParameters.of(queryParams.toSingleValueMap());
            } catch (MissingUtmParameterException mupe) {
                this.utmParameters = null;
            }
        }

        queryParams.remove(UTM_SOURCE);
        queryParams.remove(UTM_MEDIUM);
        queryParams.remove(UTM_CAMPAIGN);
        queryParams.remove(UTM_TERM);
        queryParams.remove(UTM_CONTENT);

        UriComponents baseUriComponents = UriComponentsBuilder
                .newInstance()
                .scheme(uriComponents.getScheme())
                .userInfo(uriComponents.getUserInfo())
                .host(uriComponents.getHost())
                .port(uriComponents.getPort())
                .path(uriComponents.getPath())
                .queryParams(queryParams)
                .fragment(uriComponents.getFragment())
                .build();

        try {
            longUrl = baseUriComponents.toUri();
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new InvalidUrlException("Couldn't build longUrl", e);
        }

        if (this.utmParameters != null) {
            queryParams.setAll(this.utmParameters.asMap());
        }

        try {
            targetUrl = UriComponentsBuilder
                    .newInstance()
                    .uriComponents(baseUriComponents)
                    .queryParams(queryParams)
                    .build()
                    .toUri();
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new InvalidUrlException("Couldn't build longUrl", e);
        }
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

    public URI getTargetUri() {
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
        return getTargetUri().toString();
    }

}
