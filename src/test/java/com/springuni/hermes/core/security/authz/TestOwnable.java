package com.springuni.hermes.core.security.authz;

import com.springuni.hermes.user.model.Ownable;
import com.springuni.hermes.user.model.UserId;
import lombok.Value;

@Value(staticConstructor = "of")
public class TestOwnable implements Ownable {

    private final UserId userId;

}