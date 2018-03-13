package com.springuni.hermes.user;

import java.util.Optional;

public interface UserService {

    Optional<User> getCurrentUser();

    void setCurrentUser(User user);

}
