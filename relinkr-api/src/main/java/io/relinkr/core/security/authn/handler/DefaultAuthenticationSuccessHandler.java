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

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.OK;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.relinkr.core.security.authn.jwt.JwtAuthenticationService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Created by lcsontos on 5/17/17.
 */
@Slf4j
public class DefaultAuthenticationSuccessHandler
    extends AbstractAuthenticationRequestHandler implements AuthenticationSuccessHandler {

  static final int ONE_DAY_MINUTES = 24 * 60;

  private final JwtAuthenticationService jwtAuthenticationService;

  @Autowired
  public DefaultAuthenticationSuccessHandler(
      ObjectMapper objectMapper, JwtAuthenticationService jwtAuthenticationService) {

    super(objectMapper.writer());
    this.jwtAuthenticationService = jwtAuthenticationService;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    try {
      String jwtToken = jwtAuthenticationService
          .createJwtToken(authentication, ONE_DAY_MINUTES);

      handle(response, OK, singletonMap("token", jwtToken));
    } catch (Exception ex) {
      handleError(response, ex);
    }
  }

}
