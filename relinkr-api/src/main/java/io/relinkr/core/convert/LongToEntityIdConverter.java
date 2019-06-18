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

import io.relinkr.core.orm.AbstractId;
import java.io.Serializable;

public class LongToEntityIdConverter<T extends AbstractId<?>>
    extends AbstractEntityIdConverter<Long, T> {

  public LongToEntityIdConverter(Class<T> targetClass) {
    super(targetClass);
  }

  @Override
  protected Serializable preProcessSource(Long source) {
    return source;
  }

}
