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

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.relinkr.core.web.RestErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Used by {@link JwtAuthenticationFilter} when the given JWT token cannot be used to grant access
 * to a protected resource.
 */
@Slf4j
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final ObjectWriter objectWriter;

  public JwtAuthenticationFailureHandler(ObjectMapper objectMapper) {
    this.objectWriter = objectMapper.writer();
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception)
      throws IOException {

    log.warn(exception.getMessage());

    HttpStatus httpStatus = translateAuthenticationException(exception);

    response.setStatus(httpStatus.value());
    response.setContentType(APPLICATION_JSON_VALUE);

    objectWriter.writeValue(response.getWriter(), RestErrorResponse.of(httpStatus, exception));
  }

  private HttpStatus translateAuthenticationException(AuthenticationException exception) {
    if (exception instanceof InternalAuthenticationServiceException) {
      return INTERNAL_SERVER_ERROR;
    }

    if (exception instanceof AuthenticationServiceException) {
      return SERVICE_UNAVAILABLE;
    }

    return UNAUTHORIZED;
  }

}
