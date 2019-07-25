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

import static io.relinkr.test.Mocks.FIXED_CLOCK;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

public class TimeSpanFactoryTest {

  private final TimeSpanFactory timeSpanFactory = new TimeSpanFactory(FIXED_CLOCK);

  @Test
  public void shouldContainToday() {
    fail();
  }

  @Test
  public void shouldContainTodayAndYesterday() {
    fail();
  }

  @Test
  public void shouldContainDaysOfThisWeek() {
    fail();
  }

  @Test
  public void shouldContainDaysOfPastWeek() {
    fail();
  }

  @Test
  public void shouldContainDaysOfThisMonth() {
    fail();
  }

  @Test
  public void shouldContainDaysOfPastMonth() {
    fail();
  }

  @Test
  public void shouldContainDaysOfThisQuarter() {
    fail();
  }

  @Test
  public void shouldContainDaysOfPastQuarter() {
    fail();
  }

  @Test
  public void shouldContainDaysOfThisYear() {
    fail();
  }

  @Test
  public void shouldContainDaysOfPastYear() {
    fail();
  }

}
