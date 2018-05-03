package com.springuni.hermes.core.security.authn;

import com.springuni.hermes.core.security.authn.AbstractWebSecurityTest.TestConfig;
import com.springuni.hermes.user.service.UserProfileFactory;
import com.springuni.hermes.user.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public abstract class AbstractWebSecurityTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected UserProfileFactory userProfileFactory;

    @MockBean
    protected UserService userService;

    @MockBean
    protected ClientRegistrationRepository clientRegistrationRepository;

    @TestConfiguration
    @Import(WebSecurityConfig.class)
    public static class TestConfig {

    }

}
