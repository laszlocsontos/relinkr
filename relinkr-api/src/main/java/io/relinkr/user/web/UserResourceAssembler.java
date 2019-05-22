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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import io.relinkr.user.model.User;
import io.relinkr.user.model.UserProfileType;
import lombok.NonNull;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {

  public UserResourceAssembler() {
    super(UserResourceController.class, UserResource.class);
  }

  @Override
  public UserResource toResource(@NonNull User user) {
    return toResource(user, null);
  }

  /**
   * Creates a new DTO from the given {@link User} and {@code UserProfileType}.
   *
   * @param user A {@code User} entity (cannot be null)
   * @param userProfileType A {@code UserProfileType} value (can be null).
   * @return A newly created {@code UserResource} DTO
   */
  public UserResource toResource(@NonNull User user, UserProfileType userProfileType) {
    UserResource userResource = new UserResource(user, userProfileType);
    userResource.add(linkTo(UserResourceController.class).slash(user.getId()).withSelfRel());
    return userResource;
  }

}
