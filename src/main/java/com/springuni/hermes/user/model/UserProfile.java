package com.springuni.hermes.user.model;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PACKAGE;

import com.springuni.hermes.core.model.TimeZone;
import java.net.URI;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
@ToString(of = {"userProfileType", "fullName"})
public class UserProfile {

    @Enumerated(STRING)
    @Column(name = "user_profile_type")
    private UserProfileType userProfileType;

    private String fullName;
    private String givenName;
    private String middleName;
    private String familyName;

    private URI profileUrl;
    private URI pictureUrl;

    @Enumerated(STRING)
    private Gender gender;

    private LocalDate birthDate;

    public UserProfileType getUserProfileType() {
        return userProfileType;
    }

    public Optional<String> getFullName() {
        return Optional.ofNullable(fullName);
    }

    public Optional<String> getGivenName() {
        return Optional.ofNullable(givenName);
    }

    public Optional<String> getMiddleName() {
        return Optional.ofNullable(middleName);
    }

    public Optional<String> getFamilyName() {
        return Optional.ofNullable(familyName);
    }

    public Optional<URI> getProfileUrl() {
        return Optional.ofNullable(profileUrl);
    }

    public Optional<URI> getPictureUrl() {
        return Optional.ofNullable(pictureUrl);
    }

    public Optional<Gender> getGender() {
        return Optional.ofNullable(gender);
    }

    public Optional<LocalDate> getBirthDate() {
        return Optional.ofNullable(birthDate);
    }

}
