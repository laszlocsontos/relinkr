package com.springuni.hermes.link.web;

import static org.springframework.http.HttpHeaders.readOnlyHttpHeaders;
import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.core.model.EntityNotFoundException;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.RedirectedEvent;
import com.springuni.hermes.link.service.LinkService;
import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.visitor.model.VisitorId;
import com.springuni.hermes.visitor.service.VisitorService;
import com.springuni.hermes.visitor.web.VisitorIdResolver;
import java.net.URI;
import java.time.Clock;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.ServletWebRequest;

@Controller
public class RedirectController {

    static final String REDIRECT_NOT_FOUND_URL_PROPERTY =
            "craftingjava.relinkr.redirect.not-found-url";

    private static final String EMIT_REDIRECT_EVENT_CALLBACK = "EMIT_REDIRECT_EVENT";
    private static final HttpHeaders HTTP_HEADERS;

    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;
    private final URI notFoundUrl;

    private final LinkService linkService;
    private final VisitorIdResolver visitorIdResolver;
    private final VisitorService visitorService;

    static {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setCacheControl("no-cache, no-store, max-age=0, must-revalidate");
        httpHeaders.setExpires(0);
        httpHeaders.setPragma("no-cache");

        HTTP_HEADERS = readOnlyHttpHeaders(httpHeaders);
    }

    public RedirectController(
            ApplicationEventPublisher eventPublisher,
            ObjectProvider<Clock> clockProvider,
            Environment environment,
            LinkService linkService,
            VisitorIdResolver visitorIdResolver,
            VisitorService visitorService) {

        this.eventPublisher = eventPublisher;
        clock = clockProvider.getIfAvailable(Clock::systemUTC);
        notFoundUrl = environment.getRequiredProperty(REDIRECT_NOT_FOUND_URL_PROPERTY, URI.class);
        this.linkService = linkService;
        this.visitorIdResolver = visitorIdResolver;
        this.visitorService = visitorService;
    }

    @GetMapping("/{path}")
    public ResponseEntity redirectLink(
            @PathVariable(required = false) String path, ServletWebRequest webRequest)
            throws ApplicationException {

        if (StringUtils.isEmpty(path)) {
            return buildRedirect(notFoundUrl);
        }

        Link link;
        try {
            link = linkService.getLink(path);
        } catch (EntityNotFoundException e) {
            return buildRedirect(notFoundUrl);
        }

        emitRedirectedEvent(link, webRequest);

        return buildRedirect(link.getTargetUrl());
    }

    private ResponseEntity buildRedirect(URI targetUrl) {
        return ResponseEntity
                .status(MOVED_PERMANENTLY)
                .headers(HTTP_HEADERS)
                .location(targetUrl)
                .build();
    }

    private void emitRedirectedEvent(Link link, ServletWebRequest webRequest) {
        UserId userId = link.getUserId();

        VisitorId existingVisitorId =
                visitorIdResolver.resolveVisitorId(webRequest.getRequest()).orElse(null);

        VisitorId visitorId = visitorService.ensureVisitor(existingVisitorId, userId);

        if (!visitorId.equals(existingVisitorId)) {
            visitorIdResolver.setVisitorId(webRequest.getResponse(), visitorId);
        }

        RedirectedEvent redirectedEvent =
                RedirectedEvent.of(link.getId(), visitorId, userId, clock.instant());

        webRequest.registerDestructionCallback(
                EMIT_REDIRECT_EVENT_CALLBACK,
                () -> eventPublisher.publishEvent(redirectedEvent),
                SCOPE_REQUEST
        );
    }

}
