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

package io.relinkr.link.web;

import static io.relinkr.link.web.RedirectController.FRONT_END_LOGIN_URL_PROPERTY;
import static io.relinkr.link.web.RedirectController.HEADER_XFF;
import static io.relinkr.link.web.RedirectController.REDIRECT_NOT_FOUND_URL_PROPERTY;
import static io.relinkr.test.Mocks.FIXED_CLOCK;
import static io.relinkr.test.Mocks.FRONTEND_LOGIN_URL;
import static io.relinkr.test.Mocks.NOT_FOUND_URL;
import static io.relinkr.test.Mocks.createLink;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.EXPIRES;
import static org.springframework.http.HttpHeaders.PRAGMA;
import static org.springframework.mock.web.MockHttpServletRequest.DEFAULT_SERVER_ADDR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.link.model.Link;
import io.relinkr.link.service.LinkService;
import io.relinkr.link.web.RedirectControllerTest.TestConfig;
import java.time.Clock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource(properties = {
    FRONT_END_LOGIN_URL_PROPERTY + "=" + FRONTEND_LOGIN_URL,
    REDIRECT_NOT_FOUND_URL_PROPERTY + "=" + NOT_FOUND_URL
})
@WebMvcTest(controllers = RedirectController.class, secure = false)
public class RedirectControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LinkService linkService;

  private Link link;

  @Before
  public void setUp() {
    link = createLink();
  }

  @Test
  public void givenNonExistentPath_whenRedirect_thenNotFound() throws Exception {
    given(linkService.getLink(link.getPath()))
        .willThrow(new EntityNotFoundException("path", link.getPath()));

    redirect(link.getPath(), NOT_FOUND_URL);

    then(linkService).should().getLink(link.getPath());
  }

  @Test
  public void givenExistentPath_whenRedirect_thenNotFound() throws Exception {
    given(linkService.getLink(link.getPath())).willReturn(link);

    redirect(link.getPath(), link.getTargetUrl().toString());

    then(linkService).should().getLink(link.getPath());
  }

  @Test
  public void givenRootPath_whenHandleRoot_thenRedirectedToLogin() throws Exception {
    redirect("", FRONTEND_LOGIN_URL);
  }

  private void redirect(String path, String targetUrl) throws Exception {
    mockMvc.perform(get("/" + path).header(HEADER_XFF, DEFAULT_SERVER_ADDR))
        .andDo(print())
        .andExpect(status().isMovedPermanently())
        .andExpect(header().string(CACHE_CONTROL,
            "no-cache, no-store, max-age=0, must-revalidate"))
        .andExpect(header().string(EXPIRES, "Thu, 01 Jan 1970 00:00:00 GMT"))
        .andExpect(header().string(PRAGMA, "no-cache"))
        .andExpect(redirectedUrl(targetUrl));
  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    Clock clock() {
      return FIXED_CLOCK;
    }

  }

}
