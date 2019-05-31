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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Provides the same functionality as {@link JwtAuthenticationFailureHandler} does, except that
 * it used by {@link org.springframework.security.web.access.ExceptionTranslationFilter} at that
 * stage when {@link JwtAuthenticationFilter} hasn't created a
 * {@link org.springframework.security.core.context.SecurityContext} for the current call.
 */
public class JwtAuthenticationEntryPoint
    extends JwtAuthenticationFailureHandler implements AuthenticationEntryPoint {

  public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
    super(objectMapper);
  }

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    onAuthenticationFailure(request, response, authException);
  }

}
