package com.springuni.hermes.user.service;

import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.Role;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.model.UserId;
import java.util.Optional;

public interface UserService {

    Optional<User> findUser(UserId userId);

    Optional<User> findUser(EmailAddress emailAddress);

    User addUser(String emailAddress, CharSequence rawPassword, String name, String twitterHandle);

    void deleteUser(UserId userId);

    void confirmUser(UserId userId);

    void lockUser(UserId userId);

    void unlockUser(UserId userId);

    void grantRole(UserId userId, Role role);

    void revokeRole(UserId userId, Role role);

}
