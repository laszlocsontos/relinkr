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

import io.relinkr.click.model.IpAddress;
import io.relinkr.core.model.Country;
import java.util.Optional;

/**
 * Locates the {@link Country} of origin of IP addresses.
 */
public interface GeoLocator {

  /**
   * Given an {@link IpAddress} this method locates its country of origin.
   *
   * @param ipAddress IP address
   * @return A {@code Country} when it's identifiable, otherwise result will be empty
   */
  Optional<Country> lookupCountry(IpAddress ipAddress);

}
