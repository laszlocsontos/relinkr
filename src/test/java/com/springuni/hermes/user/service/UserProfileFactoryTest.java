package com.springuni.hermes.user.service;

import static com.springuni.hermes.Mocks.GOOGLE_USER_ATTRIBUTES;
import static com.springuni.hermes.user.model.UserProfileType.FACEBOOK;
import static com.springuni.hermes.user.model.UserProfileType.GOOGLE;
import static org.junit.Assert.assertEquals;

import com.springuni.hermes.user.model.UserProfile;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class UserProfileFactoryTest {

    private final UserProfileFactory userProfileFactory = new UserProfileFactoryImpl();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void givenGoogleUserInfo_whenCreate_thenOk() {
        UserProfile userProfile = userProfileFactory.create(GOOGLE, GOOGLE_USER_ATTRIBUTES);

        assertEquals("12345789", userProfile.getUserProfileId());
        assertEquals("László Csontos", userProfile.getFullName().get());
        assertEquals("László", userProfile.getGivenName().get());
        assertEquals("Csontos", userProfile.getFamilyName().get());
        assertEquals(URI.create("https://plus.google.com/104401221461109262503"),
                userProfile.getProfileUrl().get());
        assertEquals(URI.create(
                "https://lh3.googleusercontent.com/-7EVTpxqEgj8/AAAAAAAAAAI/AAAAAAAAAAA/Qo9wrOAoxPU/photo.jpg"),
                userProfile.getPictureUrl().get());
    }

    @Test
    public void givenFacebookUserInfo_whenCreate_thenOk() {
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("id", "12345789");
        userAttributes.put("name", "László Csontos");
        userAttributes.put("first_name", "László");
        userAttributes.put("last_name", "Csontos");
        userAttributes.put("link", "https://www.facebook.com/app_scoped_user_id/807050522759429/");
        userAttributes.put("profile_pic",
                "https://lh3.googleusercontent.com/-7EVTpxqEgj8/AAAAAAAAAAI/AAAAAAAAAAA/Qo9wrOAoxPU/photo.jpg");
        userAttributes.put("birthday", "05/06");

        UserProfile userProfile = userProfileFactory.create(FACEBOOK, userAttributes);

        assertEquals("12345789", userProfile.getUserProfileId());
        assertEquals("László Csontos", userProfile.getFullName().get());
        assertEquals("László", userProfile.getGivenName().get());
        assertEquals("Csontos", userProfile.getFamilyName().get());
        assertEquals(URI.create("https://www.facebook.com/app_scoped_user_id/807050522759429/"),
                userProfile.getProfileUrl().get());
        assertEquals(URI.create(
                "https://lh3.googleusercontent.com/-7EVTpxqEgj8/AAAAAAAAAAI/AAAAAAAAAAA/Qo9wrOAoxPU/photo.jpg"),
                userProfile.getPictureUrl().get());
    }

}
