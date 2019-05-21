package io.relinkr.user.service;

import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.model.Role;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserId;
import io.relinkr.user.model.UserProfile;
import java.util.Optional;

public interface UserService {

  User saveUser(EmailAddress emailAddress, UserProfile userProfile);

  User getUser(UserId userId);

  Optional<User> findUser(EmailAddress emailAddress);

  void deleteUser(UserId userId);

  void lockUser(UserId userId);

  void unlockUser(UserId userId);

  void grantRole(UserId userId, Role role);

  void revokeRole(UserId userId, Role role);

}
