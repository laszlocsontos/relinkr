package io.relinkr.core.security.authz.access;

import io.relinkr.user.model.Ownable;
import io.relinkr.user.model.UserId;
import lombok.Value;

@Value(staticConstructor = "of")
public class TestOwnable implements Ownable {

  private final UserId userId;

}
