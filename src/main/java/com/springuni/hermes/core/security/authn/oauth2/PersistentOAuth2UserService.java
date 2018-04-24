package com.springuni.hermes.core.security.authn.oauth2;

import static java.util.stream.Collectors.toSet;

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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class PersistentOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    static final String USER_ID = "_user_id";
    static final String USER_PROFILE_TYPE = "_user_profile_type";

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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultUserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        UserProfileType userProfileType = UserProfileType.valueOf(registrationId.toUpperCase());

        UserProfile userProfile = userProfileFactory
                .create(userProfileType, oAuth2User.getAttributes());

        User user = userService.ensureUser(EmailAddress.of(oAuth2User.getName()), userProfile);

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::name)
                .map("ROLE_"::concat)
                .map(SimpleGrantedAuthority::new)
                .collect(toSet());

        Map<String, Object> userAttributes = new HashMap<>(oAuth2User.getAttributes());

        userAttributes.put(USER_ID, user.getId().getId());
        userAttributes.put(USER_PROFILE_TYPE, userProfileType);

        return new DefaultOAuth2User(authorities, userAttributes, USER_ID);
    }

}
