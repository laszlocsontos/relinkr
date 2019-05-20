package io.relinkr.user.model;

import static io.relinkr.test.Mocks.createUser;
import static io.relinkr.test.Mocks.createUserProfile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = createUser();
    }

    @Test
    public void givenUnlockedUser_whenLock_thenLocked() {
        user.lock();
        assertTrue(user.isLocked());
    }

    @Test
    public void givenLockedUser_withUnlock_thenUnlocked() {
        user.lock();
        user.unlock();
        assertFalse(user.isLocked());
    }

    @Test
    public void givenNormal_whenGrantRole_thenGranted() {
        user.grantRole(Role.ADMIN);
        assertTrue(user.isAdmin());
    }

    @Test
    public void givenAdminRole_whenRevokeRole_thenRevoked() {
        user.grantRole(Role.ADMIN);
        user.revokeRole(Role.ADMIN);
        assertFalse(user.isAdmin());
    }

    @Test
    public void givenNoUserProfiles_whenAddUserProfile_thenProfileAdded() {
        UserProfile userProfile = createUserProfile();
        user.addUserProfile(userProfile);
        assertEquals(userProfile, user.getUserProfile(userProfile.getUserProfileType()).get());
    }

}