package com.springuni.hermes.domain.user;

import java.util.Optional;

public interface UserService {

    Optional<User> getCurrentUser();

    void setCurrentUser(User user);

}
