package com.springuni.hermes.domain.user;

import com.springuni.hermes.domain.core.BaseRepository;
import java.util.Optional;

interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByEmailAddress(EmailAddress emailAddress);

    Optional<User> findByTwitterHandle(String twitterHandle);

}
