package com.springuni.hermes.user.service;

import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.Role;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.user.model.UserProfile;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User ensureUser(EmailAddress emailAddress, UserProfile userProfile) {
        Optional<User> userOptional = findUser(emailAddress);

        User user = null;

        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = User.of(emailAddress);

            try {
                user = userRepository.save(user);
            } catch (DuplicateKeyException e) {
                // User might have been created by another concurrent process
                log.warn(e.getMessage(), e);
                user = findUser(emailAddress).get();
            }
        }

        while (true) {
            user.addUserProfile(userProfile);

            try {
                user = userRepository.save(user);
                break;
            } catch (OptimisticLockingFailureException e) {
                // User might have been altered by another concurrent process
                log.warn(e.getMessage(), e);
                user = findUser(emailAddress).get();
            }
        }

        return user;
    }

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
