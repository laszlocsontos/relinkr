package com.springuni.hermes.click.service;

import com.springuni.hermes.click.model.IpAddress;
import com.springuni.hermes.core.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestOperations;

public class GeoLocatorImpl implements GeoLocator {

    private static final String GEOJS_COUNTRY_ENDPOINT =
            "https://get.geojs.io/v1/ip/country/{ip_address}";

    private final RestOperations restOperations;

    @Autowired
    public GeoLocatorImpl(RestTemplateBuilder restTemplateBuilder) {
        this(restTemplateBuilder.build());
    }

    GeoLocatorImpl(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    @Override
    public Country lookupCountry(IpAddress ipAddress) {
        String countryCode = restOperations.getForObject(
                GEOJS_COUNTRY_ENDPOINT, String.class, ipAddress.getIpAddress()
        );

        return Country.valueOf(countryCode);
    }

}
