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

package io.relinkr.core.security.authn.jwt;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.relinkr.core.web.RestErrorResponse;
import io.relinkr.test.web.BaseServletTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFailureHandlerTest extends BaseServletTest {

  private static final String MESSAGE = "an error occurred";

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AuthenticationFailureHandler handler =
      new JwtAuthenticationFailureHandler(objectMapper);

  @Test
  public void givenBadCredentialsException_whenOnAuthenticationFailure_Unauthorized()
      throws Exception {
    handler.onAuthenticationFailure(request, response, new BadCredentialsException(MESSAGE));

    RestErrorResponse errorResponse =
        objectMapper.readValue(response.getContentAsByteArray(), RestErrorResponse.class);

    assertResponse(errorResponse, UNAUTHORIZED.value(), APPLICATION_JSON_VALUE, MESSAGE);
  }

  @Test
  public void givenInternalAuthenticationServiceException_whenOnAuthenticationFailure_Unauthorized()
      throws Exception {
    handler.onAuthenticationFailure(request, response,
        new InternalAuthenticationServiceException(MESSAGE));

    RestErrorResponse errorResponse =
        objectMapper.readValue(response.getContentAsByteArray(), RestErrorResponse.class);

    assertResponse(errorResponse, INTERNAL_SERVER_ERROR.value(), APPLICATION_JSON_VALUE,
        MESSAGE);
  }

  @Test
  public void givenAuthenticationServiceException_whenOnAuthenticationFailure_Unauthorized()
      throws Exception {
    handler.onAuthenticationFailure(request, response,
        new AuthenticationServiceException(MESSAGE));

    RestErrorResponse errorResponse =
        objectMapper.readValue(response.getContentAsByteArray(), RestErrorResponse.class);

    assertResponse(errorResponse, SERVICE_UNAVAILABLE.value(), APPLICATION_JSON_VALUE, MESSAGE);
  }

  private void assertResponse(
      RestErrorResponse errorResponse,
      int expectedStatusCode, String expectedContentType, String expectedDetailMessage) {

    assertEquals(expectedStatusCode, errorResponse.getStatusCode());
    assertEquals(expectedContentType, response.getContentType());
    assertEquals(expectedDetailMessage, errorResponse.getDetailMessage());
  }

}
