package com.springuni.hermes.user.service;

import com.springuni.hermes.user.model.User;
import java.util.Optional;

public interface UserService {

    Optional<User> findUser(long userId);

    Optional<User> getCurrentUser();

    void setCurrentUser(User user);

    User login(String username, String password);

}
