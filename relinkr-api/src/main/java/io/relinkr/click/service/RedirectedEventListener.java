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

package io.relinkr.click.service;

import static java.time.ZoneOffset.UTC;

import io.relinkr.click.model.Click;
import io.relinkr.click.model.IpAddress;
import io.relinkr.link.model.RedirectedEvent;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedirectedEventListener implements ApplicationListener<RedirectedEvent> {

  private final ClickService clickService;

  @Async
  @Override
  public void onApplicationEvent(RedirectedEvent redirectedEvent) {
    Click click = Click.of(
        redirectedEvent.getLinkId(),
        redirectedEvent.getVisitorId(),
        redirectedEvent.getUserId(),
        IpAddress.fromString(redirectedEvent.getIpAddress()),
        LocalDateTime.ofInstant(redirectedEvent.getInstant(), UTC)
    );

    clickService.logClick(click);
  }

}
