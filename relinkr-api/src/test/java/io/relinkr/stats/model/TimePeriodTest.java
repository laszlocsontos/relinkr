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

import static io.relinkr.stats.model.TimePeriod.PAST_MONTH;
import static io.relinkr.stats.model.TimePeriod.PAST_WEEK;
import static io.relinkr.stats.model.TimePeriod.PAST_YEAR;
import static io.relinkr.stats.model.TimePeriod.THIS_MONTH;
import static io.relinkr.stats.model.TimePeriod.THIS_WEEK;
import static io.relinkr.stats.model.TimePeriod.THIS_YEAR;
import static io.relinkr.stats.model.TimePeriod.TODAY;
import static io.relinkr.stats.model.TimePeriod.YESTERDAY;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import org.junit.Test;

public class TimePeriodTest {

  private static final LocalDate FIXED_DATE = LocalDate.of(2019, 8, 30);

  @Test
  public void givenDate_whenTodayToTimeSpan_thenContainsToday() {
    assertTimeSpan(
        LocalDate.of(2019, 8, 30),
        LocalDate.of(2019, 8, 30),
        TODAY.toTimeSpan(FIXED_DATE)
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullDate_whenTodayToTimeSpan_thenIllegalArgumentException() {
    TODAY.toTimeSpan(null);
  }

  @Test
  public void givenDate_whenYesterdayToTimeSpan_thenContainsYesterday() {
    assertTimeSpan(
        LocalDate.of(2019, 8, 29),
        LocalDate.of(2019, 8, 29),
        YESTERDAY.toTimeSpan(FIXED_DATE)
    );
  }

  @Test
  public void givenDate_whenThisWeekToTimeSpan_thenContainsThisWeek() {
    assertTimeSpan(
        LocalDate.of(2019, 8, 26),
        LocalDate.of(2019, 9, 1),
        THIS_WEEK.toTimeSpan(FIXED_DATE)
    );
  }

  @Test
  public void givenDate_whenPastWeekToTimeSpan_thenContainsPastWeek() {
    assertTimeSpan(
        LocalDate.of(2019, 8, 19),
        LocalDate.of(2019, 8, 25),
        PAST_WEEK.toTimeSpan(FIXED_DATE)
    );
  }

  @Test
  public void givenDate_whenThisMonthToTimeSpan_thenContainsThisMonth() {
    assertTimeSpan(
        LocalDate.of(2019, 8, 1),
        LocalDate.of(2019, 8, 31),
        THIS_MONTH.toTimeSpan(FIXED_DATE)
    );
  }

  @Test
  public void givenDate_whenPastMonthToTimeSpan_thenContainsPastMonth() {
    assertTimeSpan(
        LocalDate.of(2019, 7, 1),
        LocalDate.of(2019, 7, 31),
        PAST_MONTH.toTimeSpan(FIXED_DATE)
    );
  }

  @Test
  public void givenDate_whenThisYearToTimeSpan_thenContainsThisYear() {
    assertTimeSpan(
        LocalDate.of(2019, 1, 1),
        LocalDate.of(2019, 12, 31),
        THIS_YEAR.toTimeSpan(FIXED_DATE)
    );
  }

  @Test
  public void givenDate_whenPastYearToTimeSpan_thenContainsPastYear() {
    assertTimeSpan(
        LocalDate.of(2018, 1, 1),
        LocalDate.of(2018, 12, 31),
        PAST_YEAR.toTimeSpan(FIXED_DATE)
    );
  }

  private void assertTimeSpan(
      LocalDate expectedStartDate,
      LocalDate expectedEndDate,
      TimeSpan timeSpan) {

    assertEquals("Start date mismatch", expectedStartDate, timeSpan.getStartDate());
    assertEquals("End date mismatch", expectedEndDate, timeSpan.getEndDate());
  }

}
