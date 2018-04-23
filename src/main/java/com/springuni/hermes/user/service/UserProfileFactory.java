package com.springuni.hermes.user.service;

import com.springuni.hermes.user.model.UserProfile;
import com.springuni.hermes.user.model.UserProfileType;
import java.util.Map;

public interface UserProfileFactory {

    UserProfile create(UserProfileType userProfileType, Map<String, Object> userAttributes);

}
