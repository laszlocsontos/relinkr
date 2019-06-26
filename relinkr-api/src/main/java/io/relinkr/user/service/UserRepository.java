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

package io.relinkr.user.service;

import io.relinkr.core.orm.BaseRepository;
import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserId;
import java.util.Optional;

/**
 * Repository of {@link User}s.
 */
interface UserRepository extends BaseRepository<User, UserId> {

  /**
   * Returns a user by their {@code emailAddress}.
   *
   * @param emailAddress email address to find by
   * @return A {@link User} if exists, empty otherwise
   */
  Optional<User> findByEmailAddress(EmailAddress emailAddress);

  /**
   * Flushes all pending changes to the database.
   */
  void flush();

}