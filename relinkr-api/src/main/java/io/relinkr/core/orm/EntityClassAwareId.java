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

import java.io.Serializable;

/**
 * Represents a numeric identifier of an entity, which is also aware the entity's class being
 * identified by that number.
 *
 * @param <E> Entity's type
 */
public interface EntityClassAwareId<E> extends Serializable {

  /**
   * Returns the entity's class.
   *
   * @return entity's class
   */
  Class<E> getEntityClass();

  /**
   * Returns the entity's ID.
   *
   * @return entity's ID
   */
  Long getId();

}
