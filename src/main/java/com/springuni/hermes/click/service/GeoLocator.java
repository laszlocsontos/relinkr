package com.springuni.hermes.click.service;

import com.springuni.hermes.click.model.IpAddress;
import com.springuni.hermes.core.model.Country;
import java.util.Optional;

public interface GeoLocator {

    Optional<Country> lookupCountry(IpAddress ipAddress);

}
