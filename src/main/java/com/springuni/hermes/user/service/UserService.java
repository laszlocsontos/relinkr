package com.springuni.hermes.user.service;

import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.Role;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.user.model.UserProfile;
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
