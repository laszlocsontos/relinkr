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

package io.relinkr.core.orm;

import static java.time.Clock.systemUTC;
import static java.time.ZoneOffset.UTC;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

/**
 * Strategy for generating creation and modification date timestamps according to UTC.
 */
@Component("utcLocalDateTimeProvider")
public class UtcLocalDateTimeProvider implements DateTimeProvider {

  private final Clock clock;

  public UtcLocalDateTimeProvider() {
    this(systemUTC());
  }

  UtcLocalDateTimeProvider(Clock clock) {
    this.clock = clock;
  }

  @Override
  public Optional<TemporalAccessor> getNow() {
    Instant instant = clock.instant();
    return Optional.of(LocalDateTime.ofInstant(instant, UTC));
  }

}
