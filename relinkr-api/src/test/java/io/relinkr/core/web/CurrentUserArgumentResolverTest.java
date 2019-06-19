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

package io.relinkr.core.web;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.core.security.authn.annotation.CurrentUser;
import io.relinkr.core.web.CurrentUserArgumentResolverTest.TestConfig;
import io.relinkr.core.web.CurrentUserArgumentResolverTest.TestController;
import io.relinkr.user.model.UserId;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = TestController.class, secure = false)
@ActiveProfiles("test")
public class CurrentUserArgumentResolverTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenAuthenticatedUser_whenResolveUserId_thenOk() throws Exception {
    mockMvc.perform(
        get("/").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("1"))
        .andDo(print());
  }

  @Test
  @WithMockUser(username = "a") // USER_ID
  public void givenNonNumericUserId_whenResolveUserId_thenError() throws Exception {
    mockMvc.perform(
        get("/").contentType(APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andDo(print());
  }

  @Test
  public void givenNoAuthenticatedUser_whenResolveUserId_thenNoContent()
      throws Exception {

    mockMvc.perform(
        get("/").contentType(APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @TestConfiguration
  @Import(WebMvcConfig.class)
  static class TestConfig {

    @Bean
    TestController testController() {
      return new TestController();
    }

  }

  @RestController
  static class TestController {

    @GetMapping("/")
    HttpEntity get(@CurrentUser UserId userId) {
      return Optional.ofNullable(userId)
          .map(UserId::getId)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.noContent().build());
    }

  }

}
