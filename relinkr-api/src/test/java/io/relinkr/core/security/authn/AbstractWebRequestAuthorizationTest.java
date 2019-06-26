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

package io.relinkr.core.security.authn;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.core.security.authn.AbstractWebRequestAuthorizationTest.TestConfig;
import io.relinkr.core.security.authn.AbstractWebRequestAuthorizationTest.TestController;
import io.relinkr.test.security.AbstractWebSecurityTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest(controllers = TestController.class)
@ContextConfiguration(classes = TestConfig.class)
public abstract class AbstractWebRequestAuthorizationTest extends AbstractWebSecurityTest {

  private static final String ROOT_PATH = "/";
  private static final String TEST_SHORT_LINK_PATH = "/lAjKWlW4eJ";
  private static final String TEST_API_PATH = "/v0/links/2";

  private HttpHeaders httpHeaders;

  @Before
  public void setUp() {
    httpHeaders = new HttpHeaders();
  }

  @Test
  public void givenNoAuthentication_whenRootAccessed_thenOk() throws Exception {
    performRequest(ROOT_PATH, SC_OK);
  }

  @Test
  public void givenNoAuthentication_whenShortLinkAccessed_thenOk() throws Exception {
    performRequest(TEST_SHORT_LINK_PATH, SC_OK);
  }

  @Test
  public void givenNoAuthentication_whenApiAccessed_thenUnauthorized() throws Exception {
    performRequest(TEST_API_PATH, SC_UNAUTHORIZED);
  }

  @Test
  public void givenValidAuthentication_whenRootAccessed_thenOk() throws Exception {
    withValidAuthentication();
    performRequest(ROOT_PATH, SC_OK);
  }

  @Test
  public void givenValidAuthentication_whenShortLinkAccessed_thenOk() throws Exception {
    withValidAuthentication();
    performRequest(TEST_SHORT_LINK_PATH, SC_OK);
  }

  @Test
  public void givenValidAuthentication_whenApiAccessed_thenOk() throws Exception {
    withValidAuthentication();
    performRequest(TEST_API_PATH, SC_OK);
  }

  public void givenInvalidAuthentication_whenRootAccessed_thenStatus(int status)
      throws Exception {

    withInvalidAuthentication();
    performRequest(ROOT_PATH, status);
  }

  public void givenInvalidAuthentication_whenShortLinkAccessed_thenStatus(int status)
      throws Exception {

    withInvalidAuthentication();
    performRequest(TEST_SHORT_LINK_PATH, status);
  }

  public void givenInvalidAuthentication_whenApiAccessed_thenStatus(int status) throws Exception {
    withInvalidAuthentication();
    performRequest(TEST_API_PATH, status);
  }

  protected abstract void withValidAuthentication();

  protected abstract void withInvalidAuthentication();

  void addHttpHeader(String name, String value) {
    httpHeaders.add(name, value);
  }

  private void performRequest(String path, int expectedStatus) throws Exception {
    mockMvc.perform(get(path).headers(httpHeaders))
        .andExpect(status().is(expectedStatus))
        .andDo(print());
  }

  @RestController
  static class TestController {

    @GetMapping(ROOT_PATH)
    public HttpEntity getRoot() {
      return ResponseEntity.ok().build();
    }

    @GetMapping(TEST_SHORT_LINK_PATH)
    public HttpEntity getShortLink() {
      return ResponseEntity.ok().build();
    }

    @GetMapping(TEST_API_PATH)
    public HttpEntity getApi() {
      return ResponseEntity.ok().build();
    }

  }

  @TestConfiguration
  @Import({AbstractWebSecurityTest.TestConfig.class})
  static class TestConfig {

    @Bean
    TestController testController() {
      return new TestController();
    }

  }

}
