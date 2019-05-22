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
 
package io.relinkr.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CountryTest {

  @Test
  public void givenNullCountryCode_whenFromString_thenEmpty() {
    assertFalse(Country.fromString(null).isPresent());
  }

  @Test
  public void givenEmptyCountryCode_whenFromString_thenEmpty() {
    assertFalse(Country.fromString("").isPresent());
  }

  @Test
  public void givenIllegalCountryCode_whenFromString_thenEmpty() {
    assertFalse(Country.fromString("bad").isPresent());
  }

  @Test
  public void givenUnknownCountryCode_whenFromString_thenEmpty() {
    // The old country code of the Soviet Union should be classified as unknown
    assertEquals(Country.ZZ, Country.fromString("SU").get());
  }

  @Test
  public void givenLowercaseCountryCode_whenFromString_thenEmpty() {
    assertEquals(Country.US, Country.fromString("us").get());
  }

  @Test
  public void givenUppercaseCountryCode_whenFromString_thenEmpty() {
    assertEquals(Country.US, Country.fromString("US").get());
  }

}
