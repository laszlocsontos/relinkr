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

package io.relinkr.user.web;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.security.authz.annotation.AuthorizeRolesOrOwner;
import io.relinkr.user.model.User;
import io.relinkr.user.model.UserId;
import io.relinkr.user.model.UserProfileType;
import io.relinkr.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResourceController {

  private final UserService userService;
  private final UserResourceAssembler userResourceAssembler;

  @AuthorizeRolesOrOwner
  @GetMapping(path = "/{userId}", produces = HAL_JSON_VALUE)
  HttpEntity<UserResource> getUser(@PathVariable UserId userId, Authentication authentication)
      throws ApplicationException {

    User user = userService.getUser(userId);

    // TODO: Add a HandlerMethodArgumentResolver as a more elegant solution
    Object details = authentication.getDetails();
    Assert.isInstanceOf(
        UserProfileType.class,
        details,
        "Authentication details should be a UserProfileType instance"
    );

    return ok(userResourceAssembler.toResource(user, (UserProfileType) details));
  }

}
