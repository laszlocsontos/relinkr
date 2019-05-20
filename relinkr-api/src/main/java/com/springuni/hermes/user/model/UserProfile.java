package com.springuni.hermes.user.model;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PACKAGE;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor(staticName = "of")
@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode
@ToString(of = {"userProfileType", "fullName"})
public class UserProfile {

    @NonNull
    @Enumerated(STRING)
    @Column(name = "user_profile_type")
    private UserProfileType userProfileType;

    @NonNull
    private String userProfileId;

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

    public String getUserProfileId() {
        return userProfileId;
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
