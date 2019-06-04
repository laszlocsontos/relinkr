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

package io.relinkr.link.service;

import io.relinkr.core.orm.OwnableRepository;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import java.util.Optional;

/**
 * Repository of {@link Link}s.
 */
interface LinkRepository extends OwnableRepository<Link, LinkId> {

  /**
   * Returns a link by its {@code path}.
   *
   * @param path Path to find by
   * @return A {@link Link} if exists, empty otherwise
   */
  Optional<Link> findByPath(String path);

}
