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

import io.relinkr.click.model.Click;
import io.relinkr.click.model.IpAddress;
import io.relinkr.core.model.Country;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClickServiceImpl implements ClickService {

  private final ClickRepository clickRepository;
  private final GeoLocator geoLocator;

  @Override
  public void logClick(Click click) {
    IpAddress visitorIp = click.getVisitorIp();

    Optional<Country> country;
    try {
      country = geoLocator.lookupCountry(visitorIp);
    } catch (RestClientException rce) {
      log.error(
          "Country code of {} couldn't be determined; reason: {}.",
          visitorIp,
          rce.getMessage(),
          rce
      );
      country = Optional.empty();
    }

    if (country.isPresent()) {
      click = click.with(country.get());
    }

    clickRepository.save(click);
  }

}
