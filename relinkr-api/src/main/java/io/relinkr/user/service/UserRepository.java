package io.relinkr.user.service;

import io.relinkr.core.orm.BaseRepository;
import io.relinkr.user.model.EmailAddress;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserId;
import java.util.Optional;

interface UserRepository extends BaseRepository<User, UserId> {

    Optional<User> findByEmailAddress(EmailAddress emailAddress);

    void deleteById(UserId userId);

}
