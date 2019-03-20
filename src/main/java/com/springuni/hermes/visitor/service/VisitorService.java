package com.springuni.hermes.visitor.service;

import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.visitor.model.VisitorId;
import org.springframework.lang.NonNull;

public interface VisitorService {

    VisitorId ensureVisitor(VisitorId visitorId, @NonNull UserId userId);

}
