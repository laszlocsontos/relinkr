package com.springuni.hermes.user.service;

import com.springuni.hermes.core.orm.BaseRepository;
import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.User;
import java.util.Optional;

interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByEmailAddress(EmailAddress emailAddress);

    Optional<User> findByTwitterHandle(String twitterHandle);

}
