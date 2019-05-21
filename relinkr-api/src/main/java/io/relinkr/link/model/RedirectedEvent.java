package io.relinkr.link.model;

import io.relinkr.core.model.GenericApplicationEvent;
import io.relinkr.user.model.UserId;
import io.relinkr.visitor.model.VisitorId;
import java.time.Instant;
import lombok.Getter;

@Getter
public class RedirectedEvent extends GenericApplicationEvent<LinkId> {

  private final VisitorId visitorId;
  private final String ipAddress;
  private final UserId userId;

  private RedirectedEvent(
      LinkId linkId, VisitorId visitorId, String ipAddress, UserId userId, Instant instant) {
    super(linkId, instant);

    this.visitorId = visitorId;
    this.ipAddress = ipAddress;
    this.userId = userId;
  }

  public static RedirectedEvent of(
      LinkId linkId, VisitorId visitorId, String visitorIp, UserId userId, Instant instant) {

    return new RedirectedEvent(linkId, visitorId, visitorIp, userId, instant);
  }

  public LinkId getLinkId() {
    return getSource();
  }

}
