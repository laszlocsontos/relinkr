package com.springuni.hermes.user.service;

import com.springuni.hermes.core.orm.BaseRepository;
import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.model.UserId;
import java.util.Optional;

interface UserRepository extends BaseRepository<User, UserId> {

    Optional<User> findByEmailAddress(EmailAddress emailAddress);

}
