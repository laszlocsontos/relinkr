package com.springuni.hermes.domain.link;

import static com.springuni.hermes.domain.utm.UtmParameters.UTM_CAMPAIGN;
import static com.springuni.hermes.domain.utm.UtmParameters.UTM_CONTENT;
import static com.springuni.hermes.domain.utm.UtmParameters.UTM_MEDIUM;
import static com.springuni.hermes.domain.utm.UtmParameters.UTM_SOURCE;
import static com.springuni.hermes.domain.utm.UtmParameters.UTM_TERM;

import com.springuni.hermes.domain.utm.MissingUtmParameterException;
import com.springuni.hermes.domain.utm.UtmParameters;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Embeddable
@EqualsAndHashCode
public class LongUrl {

    private URL baseUrl;

    @Transient
    private URL targetUrl;

    @Embedded
    private UtmParameters utmParameters;

    public LongUrl(String url) throws InvalidUrlException {
        this(url, null);
    }

    public LongUrl(String url, UtmParameters utmParameters) throws InvalidUrlException {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException | NullPointerException e) {
            throw new InvalidUrlException(e);
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
            baseUrl = baseUriComponents.toUri().toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new InvalidUrlException("Couldn't build baseUrl", e);
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
                    .toUri()
                    .toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new InvalidUrlException("Couldn't build baseUrl", e);
        }
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    LongUrl() {
    }

    public LongUrl apply(UtmParameters utmParameters) {
        return from(baseUrl, utmParameters);
    }

    public URL getBaseUrl() {
        return baseUrl;
    }

    public URL getTargetUrl() {
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

    public static LongUrl from(URL url, UtmParameters utmParameters) {
        try {
            return new LongUrl(url.toString(), utmParameters);
        } catch (InvalidUrlException e) {
            // This should never happen as url itself is a valid java.net.URL.
            throw new AssertionError("Internal error: baseUrl=" + url, e);
        }
    }

}
