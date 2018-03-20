package com.springuni.hermes.user.service;

import com.springuni.hermes.user.model.User;
import java.util.Optional;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.stereotype.Service;

@Service
class UserServiceImpl implements UserService {

    private final ThreadLocal<User> currentUserThreadLocal =
            new NamedInheritableThreadLocal<>("currentUserThreadLocal");

    @Override
    public Optional<User> findUser(long userId) {
        // TODO
        return Optional.empty();
    }

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUserThreadLocal.get());
    }

    @Override
    public void setCurrentUser(User user) {
        currentUserThreadLocal.set(user);
    }

    @Override
    public User login(String username, String password) {
        return null;
    }

}
