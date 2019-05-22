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

package io.relinkr.core.security.authz;

import static io.relinkr.test.Mocks.USER_ID;
import static javax.persistence.LockModeType.NONE;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.core.convert.StringToEntityClassAwareIdConverter;
import io.relinkr.core.security.authz.MethodSecurityConfigTest.TestConfig;
import io.relinkr.core.security.authz.access.MethodSecurityTestController;
import io.relinkr.core.security.authz.access.TestId;
import io.relinkr.core.security.authz.access.TestOwnable;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = MethodSecurityTestController.class)
public class MethodSecurityConfigTest {

  private static final TestId TEST_ID = TestId.of(1L);
  private static final TestOwnable TEST_OWNABLE = TestOwnable.of(USER_ID);

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EntityManager entityManager;

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenCurrentUserIsOwner_thenAccessGranted() throws Exception {
    given(entityManager.find(TestOwnable.class, TEST_ID, NONE)).willReturn(TEST_OWNABLE);

    mockMvc.perform(
        get("/{testId}/with-annotation", TEST_ID).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(username = "0", roles = "ADMIN") // USER_ID_ZERO
  public void givenCurrentUserIsAdminButNotOwner_thenAccessGranted() throws Exception {
    given(entityManager.find(TestOwnable.class, TEST_ID, NONE)).willReturn(TEST_OWNABLE);

    mockMvc.perform(
        get("/{testId}/with-annotation", TEST_ID).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(username = "0") // USER_ID_ZERO
  public void givenCurrentUserIsNotOwner_thenAccessDenied() throws Exception {
    given(entityManager.find(TestOwnable.class, TEST_ID, NONE)).willReturn(TEST_OWNABLE);

    mockMvc.perform(
        get("/{testId}/with-annotation", TEST_ID).contentType(APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(username = "0") // USER_ID_ZERO
  public void givenMethodSecurityIsNotEnforced_thenAccessGranted() throws Exception {
    mockMvc.perform(
        get("/{testId}/without-annotation", TEST_ID).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Configuration
  @Import(MethodSecurityConfig.class)
  public static class TestConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
      registry.addConverter(String.class, TestId.class,
          new StringToEntityClassAwareIdConverter<>(TestId.class));

    }

    @Bean
    MethodSecurityTestController methodSecurityTestController() {
      return new MethodSecurityTestController();
    }

  }

}
