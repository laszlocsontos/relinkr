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

import static lombok.AccessLevel.PACKAGE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectWriter;
import io.relinkr.core.web.RestErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = PACKAGE)
class AbstractAuthenticationRequestHandler {

  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  private final ObjectWriter objectWriter;

  void handle(HttpServletResponse response, HttpStatus httpStatus, Object valueToWrite)
      throws IOException {

    doWrite(response, httpStatus, valueToWrite);
  }

  void handleError(HttpServletResponse response, Exception ex) throws IOException {
    doWrite(response, INTERNAL_SERVER_ERROR, RestErrorResponse.of(INTERNAL_SERVER_ERROR, ex));
  }

  private void doWrite(
      HttpServletResponse response, HttpStatus httpStatus, Object valueToWrite)
      throws IOException {

    if (response.isCommitted()) {
      log.debug("Response has already been committed, cannot set HTTP status.");
      return;
    }

    response.setStatus(httpStatus.value());
    response.setContentType(APPLICATION_JSON_VALUE);

    objectWriter.writeValue(response.getWriter(), valueToWrite);
  }

}
