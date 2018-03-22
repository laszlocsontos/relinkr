package com.springuni.hermes.user.service;

import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.Role;
import com.springuni.hermes.user.model.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findUser(long userId) {
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
    public void deleteUser(long userId) {

    }

    @Override
    public void confirmUser(long userId) {

    }

    @Override
    public void lockUser(long userId) {

    }

    @Override
    public void unlockUser(long userId) {

    }

    @Override
    public void grantRole(long userId, Role role) {

    }

    @Override
    public void revokeRole(long userId, Role role) {

    }

}
