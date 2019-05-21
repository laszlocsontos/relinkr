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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User saveUser(EmailAddress emailAddress, UserProfile userProfile) {
    User user = findUser(emailAddress).orElseGet(() -> createUser(emailAddress));
    return updateUser(user, userProfile);
  }

  @Override
  @Transactional(readOnly = true)
  public User getUser(UserId userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("id", userId));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findUser(EmailAddress emailAddress) {
    return userRepository.findByEmailAddress(emailAddress);
  }

  @Override
  public void deleteUser(UserId userId) {
    userRepository.deleteById(userId);
  }

  @Override
  public void lockUser(UserId userId) {
    updateUser(userId, User::lock);
  }

  @Override
  public void unlockUser(UserId userId) {
    updateUser(userId, User::unlock);
  }

  @Override
  public void grantRole(UserId userId, Role role) {
    updateUser(userId, user -> user.grantRole(role));
  }

  @Override
  public void revokeRole(UserId userId, Role role) {
    updateUser(userId, user -> user.revokeRole(role));
  }

  User getUser(EmailAddress emailAddress) {
    return userRepository.findByEmailAddress(emailAddress)
        .orElseThrow(
            () -> new EntityNotFoundException("emailAddress", emailAddress.getValue())
        );
  }

  User createUser(EmailAddress emailAddress) {
    User user = User.of(emailAddress);

    try {
      user = userRepository.save(user);
    } catch (DuplicateKeyException e) {
      // User might have been created by another concurrent process
      log.warn(e.getMessage(), e);
      user = getUser(emailAddress);
    }

    return user;
  }

  User updateUser(User user, UserProfile userProfile) {
    while (true) {
      user.addUserProfile(userProfile);

      try {
        user = userRepository.save(user);
        break;
      } catch (OptimisticLockingFailureException e) {
        // User might have been altered by another concurrent process
        log.warn(e.getMessage(), e);
        user = getUser(user.getId());
      }
    }

    return user;
  }

  void updateUser(UserId userId, Consumer<User> updater) {
    User user = getUser(userId);
    updater.accept(user);
    userRepository.save(user);
  }

}
