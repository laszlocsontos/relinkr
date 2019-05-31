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

import io.relinkr.user.model.UserId;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

/**
 * Specialization of {@link BaseRepository} in that that this one adds subsequent methods for
 * fetching entities by their owner.
 *
 * @param <E> Entity's class
 * @param <ID> Entity's ID class
 */
@NoRepositoryBean
public interface OwnableRepository
    <E extends OwnableEntity<ID>, ID extends AbstractId<E>> extends BaseRepository<E, ID> {

  @Query("select e from #{#entityName} e where e.userId = :userId order by e.createdDate desc")
  List<E> findByUserId(@Param("userId") UserId userId);

  @Query("select e from #{#entityName} e where e.userId = :userId order by e.createdDate desc")
  Page<E> findByUserId(@Param("userId") UserId userId, Pageable pageable);

}
