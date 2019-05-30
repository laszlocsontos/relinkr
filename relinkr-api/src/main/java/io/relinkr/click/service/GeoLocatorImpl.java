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

/**
 * Implementation of {@code GeoLocator} which retries {@link GeoLocator#lookupCountry(IpAddress)}
 * upon I/O and server errors.
 */
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
    // FIXME: Refactor and wrap RestClientException with GeoLocator and propagate that
    String countryCode = restOperations.getForObject(
        GEOJS_COUNTRY_ENDPOINT,
        String.class,
        singletonMap("ip_address", ipAddress.getIpAddress())
    );

    return Optional.ofNullable(countryCode).flatMap(Country::fromString);
  }

}
