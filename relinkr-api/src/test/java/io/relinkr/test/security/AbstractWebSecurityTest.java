/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

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
