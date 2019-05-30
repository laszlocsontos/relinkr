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

/**
 * Provides the service layer for {@link Click}s, that is, handles incoming clicks and provides
 * aggregated click data.
 */
public interface ClickService {

  /**
   * Logs the given {@code Click} and looks up the {@link io.relinkr.core.model.Country} of
   * residence of that {@link io.relinkr.visitor.model.Visitor} who has made the {@code Click}.
   *
   * @param click {@code Click} to log
   */
  void logClick(Click click);

  // TODO: Add further service methods for fetching click statistics

}
