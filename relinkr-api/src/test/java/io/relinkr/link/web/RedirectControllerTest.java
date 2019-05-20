package io.relinkr.link.web;

import static io.relinkr.test.Mocks.FIXED_CLOCK;
import static io.relinkr.test.Mocks.FIXED_INSTANT;
import static io.relinkr.test.Mocks.NOT_FOUND_URL;
import static io.relinkr.test.Mocks.VISITOR_ID;
import static io.relinkr.test.Mocks.VISITOR_ID_ZERO;
import static io.relinkr.test.Mocks.VISITOR_IP;
import static io.relinkr.test.Mocks.createLink;
import static io.relinkr.link.web.RedirectController.HEADER_XFF;
import static io.relinkr.link.web.RedirectController.REDIRECT_NOT_FOUND_URL_PROPERTY;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.EXPIRES;
import static org.springframework.http.HttpHeaders.PRAGMA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.RedirectedEvent;
import io.relinkr.link.service.LinkService;
import io.relinkr.link.web.RedirectControllerTest.TestConfig;
import io.relinkr.visitor.model.VisitorId;
import io.relinkr.visitor.service.VisitorService;
import io.relinkr.visitor.web.VisitorIdCookieResolver;
import java.time.Clock;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource(properties = REDIRECT_NOT_FOUND_URL_PROPERTY + "=" + NOT_FOUND_URL)
@WebMvcTest(controllers = RedirectController.class, secure = false)
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkService linkService;

    @MockBean
    private VisitorService visitorService;

    @MockBean
    private VisitorIdCookieResolver visitorIdCookieResolver;

    @Autowired
    private RedirectedEventListener redirectedEventListener;

    private Link link;

    @Before
    public void setUp() {
        link = createLink();
    }

    @After
    public void tearDown() {
        redirectedEventListener.clear();
    }

    @Test
    public void givenInvalidVisitorId_whenRedirect_thenRedirectedAndCookieSetAndEventEmitted()
            throws Exception {

        given(linkService.getLink(link.getPath())).willReturn(link);
        //  VisitorIdCookieResolver returns null when cookie value is missing or integrity check failed
        given(visitorIdCookieResolver.resolveVisitorId(any(HttpServletRequest.class)))
                .willReturn(Optional.empty());
        given(visitorService.ensureVisitor(null, link.getUserId())).willReturn(VISITOR_ID);

        redirect(link);

        then(visitorService).should().ensureVisitor(null, link.getUserId());
        then(visitorIdCookieResolver).should().setVisitorId(
                any(HttpServletResponse.class),
                eq(VISITOR_ID)
        );

        assertRedirectedEvent();
    }

    @Test
    public void givenUnknownVisitorId_whenRedirect_thenRedirectedAndCookieSetAndEventEmitted()
            throws Exception {

        given(linkService.getLink(link.getPath())).willReturn(link);
        //  VisitorIdCookieResolver returns null when cookie value is missing or integrity check failed
        given(visitorIdCookieResolver.resolveVisitorId(any(HttpServletRequest.class)))
                .willReturn(Optional.of(VISITOR_ID_ZERO));
        given(visitorService.ensureVisitor(VISITOR_ID_ZERO, link.getUserId()))
                .willReturn(VISITOR_ID);

        redirect(link);

        then(visitorService).should().ensureVisitor(VISITOR_ID_ZERO, link.getUserId());
        then(visitorIdCookieResolver).should().setVisitorId(
                any(HttpServletResponse.class),
                eq(VISITOR_ID)
        );

        assertRedirectedEvent();
    }

    @Test
    public void givenValidVisitorId_whenRedirect_thenRedirectedAndCookieSetAndEventEmitted()
            throws Exception {

        given(linkService.getLink(link.getPath())).willReturn(link);
        given(visitorIdCookieResolver.resolveVisitorId(any(HttpServletRequest.class)))
                .willReturn(Optional.of(VISITOR_ID));
        given(visitorService.ensureVisitor(VISITOR_ID, link.getUserId())).willReturn(VISITOR_ID);

        redirect(link);

        then(visitorService).should().ensureVisitor(VISITOR_ID, link.getUserId());
        then(visitorIdCookieResolver).should(never()).setVisitorId(
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

        assertTrue(redirectedEventListener.isEmpty());
    }

    private void assertRedirectedEvent() throws InterruptedException {
        RedirectedEvent redirectedEvent = redirectedEventListener.getEvent();
        assertEquals(link.getId(), redirectedEvent.getLinkId());
        assertEquals(VISITOR_ID, redirectedEvent.getVisitorId());
        assertEquals(VISITOR_IP.getIpAddress(), redirectedEvent.getIpAddress());
        assertEquals(link.getUserId(), redirectedEvent.getUserId());
        assertEquals(FIXED_INSTANT, redirectedEvent.getInstant());
    }

    private void redirect(Link link) throws Exception {
        redirect(link.getPath(), link.getTargetUrl().toString());
    }

    private void redirect(String path, String targetUrl) throws Exception {
        mockMvc.perform(get("/" + path).header(HEADER_XFF, VISITOR_IP.getIpAddress()))
                .andDo(print())
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string(CACHE_CONTROL,
                        "no-cache, no-store, max-age=0, must-revalidate"))
                .andExpect(header().string(EXPIRES, "Thu, 01 Jan 1970 00:00:00 GMT"))
                .andExpect(header().string(PRAGMA, "no-cache"))
                .andExpect(redirectedUrl(targetUrl));
    }

    static class RedirectedEventListener implements ApplicationListener<RedirectedEvent> {

        final BlockingQueue<RedirectedEvent> redirectedEvents = new LinkedBlockingQueue<>();

        @Override
        public void onApplicationEvent(RedirectedEvent event) {
            redirectedEvents.offer(event);
        }

        void clear() {
            redirectedEvents.clear();
        }

        boolean isEmpty() {
            return redirectedEvents.isEmpty();
        }

        RedirectedEvent getEvent() throws InterruptedException {
            return redirectedEvents.poll(1, SECONDS);
        }

    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        Clock clock() {
            return FIXED_CLOCK;
        }

        @Bean
        RedirectedEventListener redirectedEventListener() {
            return new RedirectedEventListener();
        }

    }

}
