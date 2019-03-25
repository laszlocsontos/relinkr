package com.springuni.hermes.user.service;

import static com.springuni.hermes.test.Mocks.EMAIL_ADDRESS;
import static com.springuni.hermes.test.Mocks.USER_ID;
import static com.springuni.hermes.test.Mocks.createUser;
import static com.springuni.hermes.test.Mocks.createUserProfile;
import static com.springuni.hermes.user.model.Role.ADMIN;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;

import com.springuni.hermes.core.model.EntityNotFoundException;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.model.UserProfile;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private User user;
    private UserProfile userProfile;

    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl(userRepository);

        user = createUser();
        userProfile = createUserProfile();

        reset(userRepository);
        given(userRepository.save(any(User.class))).will(i -> i.getArgument(0));
    }

    @Test
    public void givenNonExistentUser_whenSaveUser_thenNewCreated() {
        given(userRepository.findByEmailAddress(EMAIL_ADDRESS)).willReturn(Optional.empty());
        userService.saveUser(EMAIL_ADDRESS, userProfile);
        assertUser(
                2,
                user -> assertNotNull(user.getUserProfile(userProfile.getUserProfileType()))
        );
    }

    @Test
    public void givenExistingUser_whenSaveUser_thenUpdated() {
        given(userRepository.findByEmailAddress(EMAIL_ADDRESS)).willReturn(Optional.of(user));
        userService.saveUser(EMAIL_ADDRESS, userProfile);
        assertUser(
                1,
                user -> assertNotNull(user.getUserProfile(userProfile.getUserProfileType()))
        );
    }

    @Test(expected = EntityNotFoundException.class)
    public void givenNonExistentUser_whenGetUser_thenException() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());
        userService.getUser(USER_ID);
    }

    @Test
    public void givenUnlockedUser_whenLockUser_thenLocked() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
        userService.lockUser(USER_ID);
        assertUser(1, user -> assertTrue(user.isLocked()));
    }

    @Test
    public void givenLockedUser_whenUnlockUser_thenUnlocked() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
        userService.unlockUser(USER_ID);
        assertUser(1, user -> assertFalse(user.isLocked()));
    }

    @Test
    public void givenUserWithoutRole_whenGrantRole_thenGranted() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
        userService.grantRole(USER_ID, ADMIN);
        assertUser(1, user -> assertThat(user.getRoles(), hasItem(ADMIN)));
    }

    @Test
    public void givenUserWithRole_whenRevokeRole_thenRevoked() {
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
        userService.grantRole(USER_ID, ADMIN);
        userService.revokeRole(USER_ID, ADMIN);
        assertUser(2, user -> assertThat(user.getRoles(), not(hasItem(ADMIN))));
    }

    private void assertUser(int invocations, Consumer<User> savedUserAssertor) {
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        then(userRepository).should(times(invocations)).save(userArgumentCaptor.capture());
        savedUserAssertor.accept(userArgumentCaptor.getValue());
    }

}
