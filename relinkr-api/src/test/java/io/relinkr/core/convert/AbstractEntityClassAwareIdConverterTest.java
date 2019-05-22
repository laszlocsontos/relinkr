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
 
package io.relinkr.core.convert;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import io.relinkr.core.orm.EntityClassAwareId;
import java.io.Serializable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

public abstract class AbstractEntityClassAwareIdConverterTest<S extends Serializable, T extends EntityClassAwareId<?>> {

  protected Converter<S, T> converter;

  @Before
  public void setUp() throws Exception {
    converter = createConverter();
  }

  @Test(expected = IllegalArgumentException.class)
  public abstract void create_withWrongClass();

  @Test
  public void convert_withNull() {
    assertNull(converter.convert(null));
  }

  @Test
  public void convert_withGoodValue() {
    assertNotNull(converter.convert(getGoodValue()));
  }

  protected abstract Converter<S, T> createConverter();

  protected abstract S getGoodValue();

}
