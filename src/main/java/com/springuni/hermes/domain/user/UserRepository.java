package com.springuni.hermes.domain.user;

import com.springuni.hermes.domain.core.BaseRepository;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, UserId> {

    Optional<User> findByEmailAddress(EmailAddress emailAddress);

    Optional<User> findByTwitterHandle(String twitterHandle);

}
