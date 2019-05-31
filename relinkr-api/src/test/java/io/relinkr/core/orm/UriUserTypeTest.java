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

import static io.relinkr.test.Mocks.LONG_URL_BASE_S;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URI;
import org.hibernate.HibernateException;
import org.hibernate.type.StringRepresentableType;
import org.junit.Test;

public class UriUserTypeTest {

  private final StringRepresentableType<URI> uriUserType = new UriUserType();

  @Test
  public void givenNull_whenToString_thenNull() {
    assertNull(uriUserType.toString(null));
  }

  @Test
  public void givenUri_whenToString_thenString() {
    assertEquals(LONG_URL_BASE_S, uriUserType.toString(URI.create(LONG_URL_BASE_S)));
  }

  @Test
  public void givenValidUri_whenFromStringValue_thenUri() {
    assertEquals(URI.create(LONG_URL_BASE_S), uriUserType.fromStringValue(LONG_URL_BASE_S));
  }

  @Test(expected = HibernateException.class)
  public void givenInvalidUri_whenFromStringValue_thenUri() {
    uriUserType.fromStringValue("bad://");
  }

}
