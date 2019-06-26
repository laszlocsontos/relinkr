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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import io.relinkr.core.model.ApplicationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides the REST API for managing users.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class CheckTokenController {

  @GetMapping(path = "/checkToken", produces = APPLICATION_JSON_VALUE)
  HttpEntity<CheckTokenResponse> checkToken(Authentication authentication)
      throws ApplicationException {

    return ok(CheckTokenResponse.of(authentication.getName(), (Long) authentication.getDetails()));
  }

  @Getter
  @RequiredArgsConstructor(staticName = "of")
  private static class CheckTokenResponse {

    private final String userId;
    private final long expiresAt;

  }

}
