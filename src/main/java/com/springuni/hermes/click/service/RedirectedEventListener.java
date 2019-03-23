package com.springuni.hermes.click.service;

import static java.time.ZoneOffset.UTC;

import com.springuni.hermes.click.model.Click;
import com.springuni.hermes.click.model.IpAddress;
import com.springuni.hermes.link.model.RedirectedEvent;
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
                // TODO: Add IP address to RedirectedEvent
                IpAddress.fromString("0.0.0.0"),
                LocalDateTime.ofInstant(redirectedEvent.getInstant(), UTC)
        );

        clickService.logClick(click);
    }

}
