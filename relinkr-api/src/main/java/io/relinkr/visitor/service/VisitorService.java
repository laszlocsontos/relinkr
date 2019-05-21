package io.relinkr.visitor.service;

import io.relinkr.user.model.UserId;
import io.relinkr.visitor.model.VisitorId;
import org.springframework.lang.NonNull;

public interface VisitorService {

  VisitorId ensureVisitor(VisitorId visitorId, @NonNull UserId userId);

}
