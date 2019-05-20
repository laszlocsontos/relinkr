package io.relinkr.click.service;

import io.relinkr.click.model.IpAddress;
import io.relinkr.core.model.Country;
import java.util.Optional;

public interface GeoLocator {

    Optional<Country> lookupCountry(IpAddress ipAddress);

}
