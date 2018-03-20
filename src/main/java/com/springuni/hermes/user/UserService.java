package com.springuni.hermes.user;

import java.util.Optional;

public interface UserService {

    Optional<User> findUser(long userId);

    Optional<User> getCurrentUser();

    void setCurrentUser(User user);

    User login(String username, String password);

}
