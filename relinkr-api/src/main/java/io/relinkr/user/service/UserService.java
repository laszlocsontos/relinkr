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

import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.model.Role;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserId;
import io.relinkr.user.model.UserProfile;
import java.util.Optional;

/**
 * Provides the service layer for {@link User}s, that is, takes care of creating, updating, etc.
 */
public interface UserService {

  /**
   * Saves a user with the given {@code emailAddress} and {@code userProfile}.
   *
   * @param emailAddress user's email address
   * @param userProfile user's profile
   * @return saved user
   */
  User saveUser(EmailAddress emailAddress, UserProfile userProfile);

  /**
   * Fetches the user by its ID.
   *
   * @param userId user's ID
   * @return user if found
   * @throws EntityNotFoundException is thrown if the user doesn't exist
   */
  User getUser(UserId userId) throws EntityNotFoundException;

  /**
   * Fetches the user by its email address.
   *
   * @param emailAddress user's ID
   * @return user if found, empty otherwise
   */
  Optional<User> findUser(EmailAddress emailAddress);

  /**
   * Locks the user by its ID.
   *
   * @param userId user's ID
   * @throws EntityNotFoundException is thrown if the user doesn't exist
   */
  void lockUser(UserId userId) throws EntityNotFoundException;

  /**
   * Unlocks the user by its ID.
   *
   * @param userId user's ID
   * @throws EntityNotFoundException is thrown if the user doesn't exist
   */
  void unlockUser(UserId userId) throws EntityNotFoundException;

  /**
   * Grants {@code role} to user identified by {@code userId}.
   *
   * @param userId user's ID
   * @throws EntityNotFoundException is thrown if the user doesn't exist
   */
  void grantRole(UserId userId, Role role) throws EntityNotFoundException;

  /**
   * Revokes {@code role} from user identified by {@code userId}.
   *
   * @param userId user's ID
   * @throws EntityNotFoundException is thrown if the user doesn't exist
   */
  void revokeRole(UserId userId, Role role) throws EntityNotFoundException;

}
