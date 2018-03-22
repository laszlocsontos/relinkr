package com.springuni.hermes.user.service;

import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.Role;
import com.springuni.hermes.user.model.User;
import java.util.Optional;

public interface UserService {

    Optional<User> findUser(long userId);

    Optional<User> findUser(EmailAddress emailAddress);

    User addUser(String emailAddress, CharSequence rawPassword, String name, String twitterHandle);

    void deleteUser(long userId);

    void confirmUser(long userId);

    void lockUser(long userId);

    void unlockUser(long userId);

    void grantRole(long userId, Role role);

    void revokeRole(long userId, Role role);

}
