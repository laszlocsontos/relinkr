package com.springuni.hermes.user.model;

import static com.springuni.hermes.user.model.UserProfileType.FACEBOOK;

import org.junit.Test;

/**
 * Trivial test case to verify Lombok's magic.
 */
public class UserProfileTest {

    @Test(expected = IllegalArgumentException.class)
    public void givenNullProfileType_whenCreate_thenNPE() {
        UserProfile.of(null, "1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullProfileId_whenCreate_thenNPE() {
        UserProfile.of(FACEBOOK, null);
    }

}
