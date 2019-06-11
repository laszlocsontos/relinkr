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
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User saveUser(EmailAddress emailAddress, UserProfile userProfile) {
    User user = findUser(emailAddress).orElseGet(() -> createUser(emailAddress));
    return addUserProfile(user, userProfile);
  }

  @Override
  @Transactional(readOnly = true)
  public User getUser(UserId userId) throws EntityNotFoundException {
    return userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("id", userId));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findUser(EmailAddress emailAddress) {
    return userRepository.findByEmailAddress(emailAddress);
  }

  @Override
  public void lockUser(UserId userId) throws EntityNotFoundException {
    updateUser(userId, User::lock);
  }

  @Override
  public void unlockUser(UserId userId) throws EntityNotFoundException {
    updateUser(userId, User::unlock);
  }

  @Override
  public void grantRole(UserId userId, Role role) throws EntityNotFoundException {
    updateUser(userId, user -> user.grantRole(role));
  }

  @Override
  public void revokeRole(UserId userId, Role role) throws EntityNotFoundException {
    updateUser(userId, user -> user.revokeRole(role));
  }

  private User createUser(EmailAddress emailAddress) {
    User user = User.of(emailAddress);

    try {
      user = userRepository.save(user);
    } catch (DuplicateKeyException dke) {
      // User might have been created by another concurrent process, fetch it if that's the case
      log.warn(dke.getMessage(), dke);
      user = userRepository.findByEmailAddress(emailAddress).orElse(null);
    }

    // User must exist at this point
    Assert.notNull(user, "User should have been created");
    return user;
  }

  private User addUserProfile(User user, UserProfile userProfile) {
    while (true) {
      user.addUserProfile(userProfile);

      try {
        user = userRepository.save(user);
        userRepository.flush();

        return user;
      } catch (OptimisticLockingFailureException olfe) {
        // User might have been altered by another concurrent process
        log.warn(olfe.getMessage(), olfe);
        user = getUser(user.getId());
      }
    }
  }

  private void updateUser(UserId userId, Consumer<User> updater) {
    User user = getUser(userId);
    updater.accept(user);
    userRepository.save(user);
  }

}
