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
 
package io.relinkr.link.model;

import io.relinkr.core.model.ApplicationException;
import java.util.Set;

public class InvalidLinkStatusException extends ApplicationException {

  public InvalidLinkStatusException() {
  }

  public InvalidLinkStatusException(String message) {
    super(message);
  }

  public InvalidLinkStatusException(Throwable cause) {
    super(cause);
  }

  public InvalidLinkStatusException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Factory methods for creating a {@code InvalidLinkStatusException} for the specified link status
   * with communicating the expected link statuses.
   *
   * @param linkStatus That links status which was invalid to use
   * @param expectedLinkStatuses Expected statuses
   *
   * @return InvalidLinkStatusException (never {@code null}
   */
  public static InvalidLinkStatusException forLinkStatus(
      LinkStatus linkStatus, Set<LinkStatus> expectedLinkStatuses) {

    return new InvalidLinkStatusException(
        "For link status: " + linkStatus + "; expected: " + expectedLinkStatuses);
  }

}
