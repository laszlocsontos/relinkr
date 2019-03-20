package com.springuni.hermes.link.web;

import static com.springuni.hermes.Mocks.FIXED_INSTANT;
import static com.springuni.hermes.Mocks.NOT_FOUND_URL;
import static com.springuni.hermes.Mocks.VISITOR_ID;
import static com.springuni.hermes.Mocks.VISITOR_ID_ZERO;
import static com.springuni.hermes.Mocks.createLink;
import static java.time.Instant.ofEpochMilli;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.EXPIRES;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.springuni.hermes.core.model.EntityNotFoundException;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.RedirectedEvent;
import com.springuni.hermes.link.service.LinkService;
import com.springuni.hermes.visitor.model.VisitorId;
import com.springuni.hermes.visitor.service.VisitorService;
import com.springuni.hermes.visitor.web.VisitorIdResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RedirectController.class, secure = false)
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationEventPublisher eventPublisher;

    @Captor
    protected ArgumentCaptor<RedirectedEvent> eventCaptor;

    @MockBean
    private LinkService linkService;

    @MockBean
    private VisitorService visitorService;

    @MockBean
    private VisitorIdResolver visitorIdResolver;

    private Link link;

    @Before
    public void setUp() throws Exception {
        link = createLink();
    }

    @Test
    public void givenInvalidVisitorId_whenRedirect_thenRedirectedAndCookieSetAndEventEmitted()
            throws Exception {

        given(linkService.getLink(link.getPath())).willReturn(link);
        //  VisitorIdResolver returns null when cookie value is missing or integrity check failed
        given(visitorIdResolver.resolveVisitorId(any(HttpServletRequest.class))).willReturn(null);
        given(visitorService.ensureVisitor(null, link.getUserId())).willReturn(VISITOR_ID);

        redirect(link);

        then(visitorService).should().ensureVisitor(null, link.getUserId());
        then(visitorIdResolver).should().setVisitorId(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                eq(VISITOR_ID)
        );

        assertRedirectedEvent();
    }

    @Test
    public void givenUnknownVisitorId_whenRedirect_thenRedirectedAndCookieSetAndEventEmitted()
            throws Exception {

        given(linkService.getLink(link.getPath())).willReturn(link);
        //  VisitorIdResolver returns null when cookie value is missing or integrity check failed
        given(visitorIdResolver.resolveVisitorId(any(HttpServletRequest.class)))
                .willReturn(VISITOR_ID_ZERO);
        given(visitorService.ensureVisitor(VISITOR_ID_ZERO, link.getUserId()))
                .willReturn(VISITOR_ID);

        redirect(link);

        then(visitorService).should().ensureVisitor(null, link.getUserId());
        then(visitorIdResolver).should().setVisitorId(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                eq(VISITOR_ID)
        );

        assertRedirectedEvent();
    }

    @Test
    public void givenValidVisitorId_whenRedirect_thenRedirectedAndCookieSetAndEventEmitted()
            throws Exception {

        given(linkService.getLink(link.getPath())).willReturn(link);
        given(visitorIdResolver.resolveVisitorId(any(HttpServletRequest.class)))
                .willReturn(VISITOR_ID);
        given(visitorService.ensureVisitor(VISITOR_ID, link.getUserId())).willReturn(VISITOR_ID);

        redirect(link);

        then(visitorService).should().ensureVisitor(VISITOR_ID, link.getUserId());
        then(visitorIdResolver).should(never()).setVisitorId(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class),
                any(VisitorId.class)
        );

        assertRedirectedEvent();
    }

    @Test
    public void givenNonExistentPath_whenRedirect_thenNotFound() throws Exception {
        given(linkService.getLink(link.getPath()))
                .willThrow(new EntityNotFoundException("path", link.getPath()));

        redirect(link.getPath(), NOT_FOUND_URL);
    }

    private void assertRedirectedEvent() {
        then(eventPublisher).should().publishEvent(eventCaptor.capture());

        RedirectedEvent redirectedEvent = eventCaptor.getValue();
        assertEquals(link.getId(), redirectedEvent.getLinkId());
        assertEquals(VISITOR_ID, redirectedEvent.getVisitorId());
        assertEquals(link.getUserId(), redirectedEvent.getUserId());
        assertEquals(FIXED_INSTANT, ofEpochMilli(redirectedEvent.getTimestamp()));
    }

    private void redirect(Link link) throws Exception {
        redirect(link.getPath(), link.getTargetUrl().toString());
    }

    private void redirect(String path, String targetUrl) throws Exception {
        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string(CACHE_CONTROL, "no-cache, no-store"))
                .andExpect(header().longValue(EXPIRES, -1))
                .andExpect(redirectedUrl(targetUrl));
    }

}
