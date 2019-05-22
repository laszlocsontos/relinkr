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
 
package io.relinkr.core.security.authn.handler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 * General authentication failure handler.
 */
@Slf4j
public class DefaultAuthenticationFailureHandler
    extends AbstractAuthenticationRequestHandler implements AuthenticationFailureHandler {

  public DefaultAuthenticationFailureHandler(ObjectMapper objectMapper) {
    super(objectMapper.writer());
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception)
      throws IOException {

    log.warn(exception.getMessage());

    HttpStatus httpStatus = translateAuthenticationException(exception);
    handle(response, httpStatus, RestErrorResponse.of(httpStatus, exception));
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
