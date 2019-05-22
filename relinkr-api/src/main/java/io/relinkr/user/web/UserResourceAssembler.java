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
