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

import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * Base interface for repositories. Instead of using full blown interfaces like
 * {@link org.springframework.data.jpa.repository.JpaRepository}, a hand-picked methods are
 * implemented instead.
 *
 * @param <E> Entity's class
 * @param <ID> Entity's ID class
 */
@NoRepositoryBean
public interface BaseRepository
    <E extends AbstractEntity<ID>, ID extends AbstractId<E>> extends Repository<E, ID> {

  /**
   * Find the repository's entity based on its ID.
   *
   * @param id ID to find by
   * @return entity is exists
   */
  Optional<E> findById(ID id);

  /**
   * Saved the given entity.
   *
   * @param entity entity to save
   * @return saved entity
   */
  E save(E entity);

}
