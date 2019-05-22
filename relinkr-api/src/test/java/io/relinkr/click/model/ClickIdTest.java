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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Dummy test case to ensure that Lombok is doing its magic properly.
 */
public class ClickIdTest {

  @Test
  public void testCreate_withNoArgsConstructor() {
    new ClickId();
  }

  @Test
  public void testCreate_withOf() {
    ClickId clickId = ClickId.of(1L);
    assertNotNull(clickId);
  }

  @Test
  public void equals() {
    ClickId cid1 = ClickId.of(1L);
    ClickId cid2 = ClickId.of(1L);
    assertEquals(cid1, cid2);
  }

  @Test
  public void testHashCode() {
    ClickId cid1 = ClickId.of(1L);
    ClickId cid2 = ClickId.of(1L);
    assertEquals(cid1.hashCode(), cid2.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("1", ClickId.of(1L).toString());
  }

}
