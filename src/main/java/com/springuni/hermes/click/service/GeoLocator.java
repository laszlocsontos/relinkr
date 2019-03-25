package com.springuni.hermes.click.service;

import com.springuni.hermes.click.model.IpAddress;
import com.springuni.hermes.core.model.Country;

public interface GeoLocator {

    Country lookupCountry(IpAddress ipAddress);

}
