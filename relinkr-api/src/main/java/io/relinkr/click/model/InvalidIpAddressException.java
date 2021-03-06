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

package io.relinkr.click.model;

import org.springframework.core.NestedRuntimeException;

/**
 * Thrown when {@link IpAddress#fromString(String)} is given an invalid IP address.
 */
class InvalidIpAddressException extends NestedRuntimeException {

  InvalidIpAddressException(String message) {
    super(message);
  }

  InvalidIpAddressException(String message, Throwable cause) {
    super(message, cause);
  }

  static InvalidIpAddressException forIpAddress(String ipAddress) {
    return new InvalidIpAddressException("For IP address: " + ipAddress);
  }

}
