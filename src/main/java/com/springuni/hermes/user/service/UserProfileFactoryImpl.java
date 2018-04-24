package com.springuni.hermes.user.service;

import static com.springuni.hermes.user.model.UserProfileType.FACEBOOK;
import static com.springuni.hermes.user.model.UserProfileType.GOOGLE;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

import com.springuni.hermes.user.model.Gender;
import com.springuni.hermes.user.model.UserProfile;
import com.springuni.hermes.user.model.UserProfileType;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class UserProfileFactoryImpl implements UserProfileFactory {

    private final Map<UserProfileType, UserProfileCreator> USER_PROFILE_CREATOR_MAP;

    public UserProfileFactoryImpl() {
        Map<UserProfileType, UserProfileCreator> userProfileCreatorMap = new HashMap<>();

        userProfileCreatorMap.put(GOOGLE, new GoogleUserProfileCreator());
        userProfileCreatorMap.put(FACEBOOK, new FacebookUserProfileCreator());

        USER_PROFILE_CREATOR_MAP = Collections.unmodifiableMap(userProfileCreatorMap);
    }

    @Override
    public UserProfile create(UserProfileType userProfileType, Map<String, Object> userAttributes) {
        Assert.notNull(userProfileType, "userProfileType cannot be null");
        Assert.notNull(userAttributes, "userAttributes cannot be null");

        UserProfileCreator userProfileCreator = USER_PROFILE_CREATOR_MAP.get(userProfileType);
        Assert.notNull(userProfileCreator, "No user profile creator found");

        return userProfileCreator.create(userAttributes);
    }

    private static abstract class UserProfileCreator {

        final UserProfileType userProfileType;
        final String[] fieldNames;

        UserProfileCreator(UserProfileType userProfileType, String[] fieldNames) {
            this.userProfileType = userProfileType;
            this.fieldNames = fieldNames;
        }

        UserProfile create(Map<String, Object> userAttributes) {
            return UserProfile.of(
                    userProfileType,
                    getString(userAttributes, fieldNames[0]), // fullName
                    getString(userAttributes, fieldNames[1]), // givenName
                    getString(userAttributes, fieldNames[2]), // middleName,
                    getString(userAttributes, fieldNames[3]), // familyName,
                    getURI(userAttributes, fieldNames[4]), // profileUrl
                    getURI(userAttributes, fieldNames[5]), // pictureUrl
                    getGender(userAttributes, fieldNames[6]), // gender
                    getBirthDate(userAttributes, fieldNames[7]) // birthDate,
            );
        }

        LocalDate getBirthDate(Map<String, Object> userAttributes, String attributeName) {
            return null;
        }

        Gender getGender(Map<String, Object> userAttributes, String attributeName) {
            return Optional.ofNullable(getString(userAttributes, attributeName))
                    .map(String::toUpperCase).map(Gender::valueOf).orElse(null);
        }

        String getString(Map<String, Object> userAttributes, String attributeName) {
            if (attributeName == null) {
                return null;
            }

            return Optional.ofNullable(userAttributes.get(attributeName))
                    .map(Object::toString).orElse(null);
        }

        URI getURI(Map<String, Object> userAttributes, String attributeName) {
            return Optional.ofNullable(getString(userAttributes, attributeName))
                    .map(URI::create).orElse(null);
        }

    }

    private static class GoogleUserProfileCreator extends UserProfileCreator {

        private static final String[] FIELD_NAMES = {
                "name", "given_name", null, "family_name", "link", "picture", null, null
        };

        GoogleUserProfileCreator() {
            super(GOOGLE, FIELD_NAMES);
        }

    }

    private static class FacebookUserProfileCreator extends UserProfileCreator {

        // This is a fixed format string, like MM/DD/YYYY. However, people can control who can see
        // the year they were born separately from the month and day so this string can be only the
        // year (YYYY) or the month + day (MM/DD).
        // Reference: https://developers.facebook.com/docs/graph-api/reference/user
        private static final DateTimeFormatter BIRTH_DATE_FORMATTER =
                DateTimeFormatter.ofPattern("[MM/dd][/][yyyy]");

        // The oldest super-centenarian is 116 years old in 2018. It's quite safe to assume that
        // everyone who were born in 1900 (and before) had died already.
        private static final TemporalQuery<Integer> BIRTH_YEAR =
                it -> it.isSupported(YEAR) ? it.get(YEAR) : 1900;

        private static final TemporalQuery<Integer> BIRTH_MONTH =
                it -> it.isSupported(MONTH_OF_YEAR) ? it.get(MONTH_OF_YEAR) : 1;

        private static final TemporalQuery<Integer> BIRTH_DAY =
                it -> it.isSupported(DAY_OF_MONTH) ? it.get(DAY_OF_MONTH) : 1;

        private static final String[] FIELD_NAMES = {
                "name", "first_name", "middle_name", "last_name", "link", "profile_pic", "gender",
                "birthday"
        };

        FacebookUserProfileCreator() {
            super(GOOGLE, FIELD_NAMES);
        }

        @Override
        LocalDate getBirthDate(Map<String, Object> userAttributes, String attributeName) {
            TemporalAccessor temporalAccessor =
                    Optional.ofNullable(getString(userAttributes, attributeName))
                            .map(BIRTH_DATE_FORMATTER::parse).orElse(null);

            if (temporalAccessor == null) {
                return null;
            }

            int birthYear = temporalAccessor.query(BIRTH_YEAR);
            int birthMonth = temporalAccessor.query(BIRTH_MONTH);
            int birthDay = temporalAccessor.query(BIRTH_DAY);

            return LocalDate.of(birthYear, birthMonth, birthDay);
        }

        @Override
        Gender getGender(Map<String, Object> userAttributes, String attributeName) {
            try {
                return super.getGender(userAttributes, attributeName);
            } catch (IllegalArgumentException e) {
                // If the gender is set to a custom value, this value will be based off of the
                // preferred pronoun; it will be omitted if the preferred preferred pronoun is
                // neutral.
                // Reference: https://developers.facebook.com/docs/graph-api/reference/user
                return null;
            }
        }

    }

}