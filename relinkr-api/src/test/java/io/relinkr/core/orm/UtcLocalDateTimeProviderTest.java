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

import static io.relinkr.test.Mocks.FIXED_CLOCK;
import static io.relinkr.test.Mocks.FIXED_INSTANT;
import static java.time.ZoneOffset.ofHours;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import org.junit.Test;
import org.springframework.data.auditing.DateTimeProvider;

public class UtcLocalDateTimeProviderTest {

  private static final int OFFSET = 1;

  private DateTimeProvider dateTimeProvider = new UtcLocalDateTimeProvider(FIXED_CLOCK);

  @Test
  public void shouldGetUtcLocalDateTime() {
    TemporalAccessor now = dateTimeProvider.getNow().get();
    TemporalAccessor nowWithOffset = LocalDateTime.ofInstant(FIXED_INSTANT, ofHours(OFFSET));
    assertEquals(nowWithOffset.get(HOUR_OF_DAY) - OFFSET, now.get(HOUR_OF_DAY));
  }

}
