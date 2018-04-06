package com.springuni.hermes.user.service;

import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.Role;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.model.UserId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findUser(UserId userId) {
        // TODO
        return Optional.empty();
    }

    @Override
    public Optional<User> findUser(EmailAddress emailAddress) {
        return Optional.empty();
    }

    @Override
    public User addUser(
            String emailAddress, CharSequence rawPassword, String name, String twitterHandle) {

        return null;
    }

    @Override
    public void deleteUser(UserId userId) {

    }

    @Override
    public void confirmUser(UserId userId) {

    }

    @Override
    public void lockUser(UserId userId) {

    }

    @Override
    public void unlockUser(UserId userId) {

    }

    @Override
    public void grantRole(UserId userId, Role role) {

    }

    @Override
    public void revokeRole(UserId userId, Role role) {

    }

}
