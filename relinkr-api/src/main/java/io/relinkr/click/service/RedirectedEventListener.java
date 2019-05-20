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
