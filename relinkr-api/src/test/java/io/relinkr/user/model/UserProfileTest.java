package io.relinkr.user.model;

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
        UserProfile.of(UserProfileType.FACEBOOK, null);
    }

}
