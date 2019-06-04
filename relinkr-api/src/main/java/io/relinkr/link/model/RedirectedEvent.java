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

package io.relinkr.link.model;

import io.relinkr.core.model.GenericApplicationEvent;
import io.relinkr.user.model.UserId;
import io.relinkr.visitor.model.VisitorId;
import java.time.Instant;
import lombok.Getter;

/**
 * Emitted by {@link io.relinkr.link.web.RedirectController} when a link was access and the client
 * was redirected to its target URL.
 */
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
