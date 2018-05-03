package com.springuni.hermes.core.security.authn.oauth2;

import static java.util.stream.Collectors.toSet;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.SERVER_ERROR;

import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.Role;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.model.UserProfile;
import com.springuni.hermes.user.model.UserProfileType;
import com.springuni.hermes.user.service.UserProfileFactory;
import com.springuni.hermes.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Slf4j
public class PersistentOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    static final String USER_ID_ATTRIBUTE = "_user_id";
    static final String USER_PROFILE_TYPE_ATTRIBUTE = "_user_profile_type";

    static final String INVALID_EMAIL_ADDRESS = "invalid_email_address";
    static final String INVALID_PROFILE = "invalid_profile";

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultUserService;
    private final UserProfileFactory userProfileFactory;
    private final UserService userService;

    public PersistentOAuth2UserService(
            OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultUserService,
            UserProfileFactory userProfileFactory,
            UserService userService) {

        this.defaultUserService = defaultUserService;
        this.userProfileFactory = userProfileFactory;
        this.userService = userService;
    }

    /*
     * Used for unit testing only.
     */
    PersistentOAuth2UserService(UserProfileFactory userProfileFactory, UserService userService) {
        this(null, userProfileFactory, userService);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultUserService.loadUser(userRequest);

        EmailAddress emailAddress = extractEmailAddress(oAuth2User.getName());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        UserProfile userProfile = extractUserProfile(registrationId, oAuth2User.getAttributes());

        User user = saveUser(emailAddress, userProfile);

        return createOAuth2User(oAuth2User.getAttributes(), user, userProfile);
    }

    EmailAddress extractEmailAddress(String principalName) throws OAuth2AuthenticationException {
        try {
            return EmailAddress.of(principalName);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new OAuth2AuthenticationException(new OAuth2Error(INVALID_EMAIL_ADDRESS), e);
        }

    }

    UserProfile extractUserProfile(String registrationId, Map<String, Object> userAttributes)
            throws OAuth2AuthenticationException {

        try {
            UserProfileType userProfileType = UserProfileType.valueOf(registrationId.toUpperCase());
            return userProfileFactory.create(userProfileType, userAttributes);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            throw new OAuth2AuthenticationException(new OAuth2Error(INVALID_PROFILE), e);
        }

    }

    User saveUser(EmailAddress emailAddress, UserProfile userProfile)
            throws OAuth2AuthenticationException {

        try {
            return userService.saveUser(emailAddress, userProfile);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            throw new OAuth2AuthenticationException(new OAuth2Error(SERVER_ERROR), e);
        }
    }

    OAuth2User createOAuth2User(
            Map<String, Object> originalAttributes, User user, UserProfile userProfile) {

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::name)
                .map("ROLE_"::concat)
                .map(SimpleGrantedAuthority::new)
                .collect(toSet());

        Map<String, Object> userAttributes = new HashMap<>(originalAttributes);

        userAttributes.put(USER_ID_ATTRIBUTE, user.getId().getId());
        userAttributes.put(USER_PROFILE_TYPE_ATTRIBUTE, userProfile.getUserProfileType());

        return new DefaultOAuth2User(authorities, userAttributes, USER_ID_ATTRIBUTE);

    }

}
