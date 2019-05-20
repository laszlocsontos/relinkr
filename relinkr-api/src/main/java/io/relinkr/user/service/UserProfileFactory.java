package io.relinkr.user.service;

import io.relinkr.user.model.UserProfile;
import io.relinkr.user.model.UserProfileType;
import java.util.Map;

public interface UserProfileFactory {

    UserProfile create(UserProfileType userProfileType, Map<String, Object> userAttributes);

}
