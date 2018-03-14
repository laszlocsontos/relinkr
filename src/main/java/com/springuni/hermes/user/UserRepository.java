package com.springuni.hermes.user;

import com.springuni.hermes.core.orm.BaseRepository;
import java.util.Optional;

interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByEmailAddress(EmailAddress emailAddress);

    Optional<User> findByTwitterHandle(String twitterHandle);

}
