package io.relinkr.test.security;

import io.relinkr.core.security.authn.WebSecurityConfig;
import io.relinkr.core.security.authn.jwt.JwtConfig;
import io.relinkr.core.security.authn.oauth2.OAuth2AuthorizationRequestsCookieResolver;
import io.relinkr.test.security.AbstractWebSecurityTest.TestConfig;
import io.relinkr.user.service.UserProfileFactory;
import io.relinkr.user.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
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

  @MockBean
  protected OAuth2AuthorizationRequestsCookieResolver authorizationRequestsCookieResolver;

  @TestConfiguration
  @Import({WebSecurityConfig.class, JwtConfig.class})
  public static class TestConfig {

  }

}
