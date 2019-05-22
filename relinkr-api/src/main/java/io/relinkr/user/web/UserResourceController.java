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
