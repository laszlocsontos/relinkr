package com.springuni.hermes.domain.user;

import java.util.Optional;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.stereotype.Service;

@Service
class UserServiceImpl implements UserService {

    private final ThreadLocal<User> currentUserThreadLocal =
            new NamedInheritableThreadLocal<>("currentUserThreadLocal");

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUserThreadLocal.get());
    }

    @Override
    public void setCurrentUser(User user) {
        currentUserThreadLocal.set(user);
    }

}
