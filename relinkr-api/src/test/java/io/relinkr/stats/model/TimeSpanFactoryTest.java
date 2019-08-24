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
import static io.relinkr.test.Mocks.FIXED_CLOCK;
import static io.relinkr.test.Mocks.FIXED_DATE;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class TimeSpanFactoryTest {

  private final TimeSpanFactory timeSpanFactory = new TimeSpanFactory(FIXED_CLOCK);

  @Test
  public void shouldContainToday() {
    TimeSpan timeSpan = timeSpanFactory.period(TODAY);
    assertEquals(FIXED_DATE, timeSpan.getStartDate());
    assertEquals(FIXED_DATE, timeSpan.getEndDate());
  }

  @Test
  public void shouldContainTodayAndYesterday() {
    TimeSpan timeSpan = timeSpanFactory.period(YESTERDAY);
    assertEquals(FIXED_DATE.minusDays(1), timeSpan.getStartDate());
    assertEquals(FIXED_DATE.minusDays(1), timeSpan.getEndDate());
  }

  @Test
  public void shouldContainDaysOfThisWeek() {
    TimeSpan timeSpan = timeSpanFactory.period(THIS_WEEK);
    assertEquals(FIXED_DATE.with(DAY_OF_WEEK, MONDAY.getValue()), timeSpan.getStartDate());
    assertEquals(FIXED_DATE.with(DAY_OF_WEEK, SUNDAY.getValue()), timeSpan.getEndDate());
  }

  @Test
  public void shouldContainDaysOfPastWeek() {
    TimeSpan timeSpan = timeSpanFactory.period(PAST_WEEK);
    assertEquals(
        FIXED_DATE.with(DAY_OF_WEEK, MONDAY.getValue()).minusWeeks(1),
        timeSpan.getStartDate()
    );
    assertEquals(
        FIXED_DATE.with(DAY_OF_WEEK, SUNDAY.getValue()).minusWeeks(1),
        timeSpan.getEndDate()
    );
  }

  @Test
  public void shouldContainDaysOfThisMonth() {
    TimeSpan timeSpan = timeSpanFactory.period(THIS_MONTH);
    assertEquals(FIXED_DATE.withDayOfMonth(1), timeSpan.getStartDate());
    assertEquals(FIXED_DATE.plusMonths(1).withDayOfMonth(1).minusDays(1), timeSpan.getEndDate());
  }

  @Test
  public void shouldContainDaysOfPastMonth() {
    TimeSpan timeSpan = timeSpanFactory.period(PAST_MONTH);
    assertEquals(FIXED_DATE.minusMonths(1).withDayOfMonth(1), timeSpan.getStartDate());
    assertEquals(FIXED_DATE.withDayOfMonth(1).minusDays(1), timeSpan.getEndDate());
  }

  @Test
  @Ignore
  public void shouldContainDaysOfThisQuarter() {
    // TODO: Add support for quarters later
  }

  @Test
  @Ignore
  public void shouldContainDaysOfPastQuarter() {
    // TODO: Add support for quarters later
  }

  @Test
  public void shouldContainDaysOfThisYear() {
    TimeSpan timeSpan = timeSpanFactory.period(THIS_YEAR);
    assertEquals(FIXED_DATE.withDayOfYear(1), timeSpan.getStartDate());
    assertEquals(FIXED_DATE.plusYears(1).withDayOfYear(1).minusDays(1), timeSpan.getEndDate());
  }

  @Test
  public void shouldContainDaysOfPastYear() {
    TimeSpan timeSpan = timeSpanFactory.period(PAST_YEAR);
    assertEquals(FIXED_DATE.minusYears(1).withDayOfYear(1), timeSpan.getStartDate());
    assertEquals(FIXED_DATE.withDayOfYear(1).minusDays(1), timeSpan.getEndDate());
  }

}
