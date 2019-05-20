package io.relinkr.click.service;

import static java.util.Collections.singletonMap;

import io.relinkr.click.model.IpAddress;
import io.relinkr.core.model.Country;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;

@Component
public class GeoLocatorImpl implements GeoLocator {

    static final String GEOJS_COUNTRY_ENDPOINT = "https://get.geojs.io/v1/ip/country/{ip_address}";
    static final int MAX_ATTEMPTS = 5;

    private final RestOperations restOperations;

    @Autowired
    public GeoLocatorImpl(RestTemplateBuilder restTemplateBuilder) {
        this(restTemplateBuilder.build());
    }

    GeoLocatorImpl(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    @Override
    @Retryable(
            include = {ResourceAccessException.class, HttpServerErrorException.class},
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = 1_000, multiplier = 2, maxDelay = 4_000)
    )
    public Optional<Country> lookupCountry(@NonNull IpAddress ipAddress) {
        String countryCode = restOperations.getForObject(
                GEOJS_COUNTRY_ENDPOINT,
                String.class,
                singletonMap("ip_address", ipAddress.getIpAddress())
        );

        return Optional.ofNullable(countryCode).flatMap(Country::fromString);
    }

}
