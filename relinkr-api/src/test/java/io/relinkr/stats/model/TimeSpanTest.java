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

package io.relinkr.stats.model;

import static io.relinkr.stats.model.TimePeriod.CUSTOM;
import static io.relinkr.stats.model.TimePeriod.TODAY;
import static io.relinkr.test.Mocks.FIXED_DATE;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TimeSpanTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void givenNullPeriod_whenCreate_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("period is marked @NonNull but is null");

    TimeSpan.of(null, FIXED_DATE, FIXED_DATE);
  }

  @Test
  public void givenNullStartDate_whenCreate_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("startDate is marked @NonNull but is null");

    TimeSpan.of(TODAY, null, FIXED_DATE);
  }

  @Test
  public void givenNullEndDate_whenCreate_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("endDate is marked @NonNull but is null");

    TimeSpan.of(TODAY, FIXED_DATE, null);
  }

  @Test
  public void givenValidStartAndEndDate_whenCreate_thenCustomSpanCreated() {
    TimeSpan timeSpan = TimeSpan.ofCustom(FIXED_DATE, FIXED_DATE);

    assertEquals(CUSTOM, timeSpan.getPeriod());
    assertEquals(FIXED_DATE, timeSpan.getStartDate());
    assertEquals(FIXED_DATE, timeSpan.getEndDate());
  }

  @Test
  public void givenInvalidStartAndEndDate_whenCreate_thenIllegalArgumentException() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("endDate cannot be earlier that startDate");

    TimeSpan.of(TODAY, FIXED_DATE, FIXED_DATE.minusDays(1));
  }

  @Test
  public void givenDate_whenGetAllDates_thenContainsAll() {
    TimeSpan timeSpan = TimeSpan.ofCustom(FIXED_DATE, FIXED_DATE.plusDays(2));
    assertThat(
        timeSpan.getAllDates(),
        contains(FIXED_DATE, FIXED_DATE.plusDays(1), FIXED_DATE.plusDays(2))
    );
  }

}
