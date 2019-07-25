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

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeSpanTest {

  @Test(expected = IllegalArgumentException.class)
  public void givenNullPeriod_whenCreate_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullStartDate_whenCreate_thenIllegalArgumentException() {

  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullEndDate_whenCreate_thenIllegalArgumentException() {

  }

  @Test
  public void givenValidStartAndEndDate_whenCreate_thenCustomTimeSpanCreated() {
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenInvalidStartAndEndDate_whenCreate_thenCustomTimeSpanCreated() {
    // This means that end date is before the start date
  }

  @Test
  public void givenPeriodAndValidStartAndEndDate_whenCreate_thenTimeSpanWithPeriodCreated() {
    fail();
  }

}
