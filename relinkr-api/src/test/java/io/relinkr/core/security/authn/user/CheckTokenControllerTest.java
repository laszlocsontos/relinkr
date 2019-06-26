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

package io.relinkr.core.security.authn.user;

import static io.relinkr.test.Mocks.EMAIL_ADDRESS;
import static io.relinkr.test.Mocks.FIXED_INSTANT;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.test.security.AbstractResourceControllerTest;
import io.relinkr.test.security.AbstractResourceControllerTest.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = CheckTokenController.class)
public class CheckTokenControllerTest extends AbstractResourceControllerTest {

  @Test
  public void givenAuthenticatedRequest_whenCheckToken_thenUserIdAndExpReturned() throws Exception {
    withUser(EMAIL_ADDRESS, FIXED_INSTANT.getEpochSecond());

    mockMvc
        .perform(get("/v1/users/checkToken"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$.userId", is(EMAIL_ADDRESS.getValue())))
        .andExpect(jsonPath("$.expiresAt", is(FIXED_INSTANT.getEpochSecond())));
  }

  @Test
  public void givenUnauthenticatedRequest_whenCheckToken_thenUnauthorized() throws Exception {
    ResultActions resultActions = mockMvc
        .perform(get("/v1/users/checkToken"))
        .andDo(print());

    assertError(
        SC_UNAUTHORIZED,
        "Full authentication is required to access this resource",
        resultActions
    );
  }

}
