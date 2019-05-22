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

import static io.relinkr.test.Mocks.LINK_ID;
import static io.relinkr.test.Mocks.TIMESTAMP;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.VISITOR_ID;
import static io.relinkr.test.Mocks.VISITOR_IP;
import static org.junit.Assert.assertEquals;

import io.relinkr.core.model.Country;
import org.junit.Before;
import org.junit.Test;

public class ClickTest {

  private Click click;

  @Before
  public void setUp() throws Exception {
    click = Click.of(LINK_ID, VISITOR_ID, USER_ID, VISITOR_IP, TIMESTAMP);
  }

  @Test
  public void shouldHaveVisitDayOfMonth() {
    assertEquals(TIMESTAMP.getDayOfMonth(), click.getVisitDayOfMonth());
  }

  @Test
  public void shouldHaveVisitDayOfWeek() {
    assertEquals(TIMESTAMP.getDayOfWeek().getValue(), click.getVisitDayOfWeek());
  }

  @Test
  public void shouldHaveVisitHour() {
    assertEquals(TIMESTAMP.getHour(), click.getVisitHour());
  }

  @Test
  public void getVisitMonth() {
    assertEquals(TIMESTAMP.getMonth().getValue(), click.getVisitMonth());
  }

  @Test
  public void shouldHaveVisitTimestamp() {
    assertEquals(TIMESTAMP, click.getVisitTimestamp());
  }

  @Test
  public void shouldHaveCountry() {
    assertEquals(Country.US, click.with(Country.US).getCountry().get());
  }

}
