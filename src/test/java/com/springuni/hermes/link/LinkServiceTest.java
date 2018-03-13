package com.springuni.hermes.link;

import static com.springuni.hermes.Mocks.LINK_ID;
import static com.springuni.hermes.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.Mocks.LONG_URL_VALID_UTM_S;
import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_FULL;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_MINIMAL;
import static com.springuni.hermes.link.LinkStatus.ACTIVE;
import static com.springuni.hermes.link.LinkStatus.ARCHIVED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Pageable.unpaged;

import com.springuni.hermes.core.ApplicationException;
import com.springuni.hermes.core.EntityNotFoundException;
import com.springuni.hermes.Mocks;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@RunWith(MockitoJUnitRunner.class)
public class LinkServiceTest {

    @Mock
    private LinkRepository<Link> linkRepository;

    @Mock
    private StandaloneLinkRepository standaloneLinkRepository;

    private LinkService linkService;
    private EmbeddedLink embeddedLink;
    private StandaloneLink standaloneLink;
    private List<Link> links;

    @Before
    public void setUp() throws Exception {
        linkService = new LinkServiceImpl(linkRepository, standaloneLinkRepository);
        embeddedLink = Mocks.createEmbeddedLink();
        standaloneLink = Mocks.createStandaloneLink();
        links = new ArrayList<>(2);
        links.add(embeddedLink);
        links.add(standaloneLink);
    }

    @Test
    public void getTargetUrl() throws ApplicationException {
        String path = standaloneLink.getPath();
        when(linkRepository.findByPath(path)).thenReturn(Optional.of(standaloneLink));
        URL targetUrl = linkService.getTargetUrl(path);
        assertEquals(standaloneLink.getTargetUrl(), targetUrl);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getTargetUrl_withNonExistent() throws ApplicationException {
        String path = "bad path";
        when(linkRepository.findByPath(path)).thenReturn(Optional.empty());
        linkService.getTargetUrl(path);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getTargetUrl_withArchived() throws ApplicationException {
        standaloneLink.markArchived();
        String path = standaloneLink.getPath();
        when(linkRepository.findByPath(path)).thenReturn(Optional.of(standaloneLink));
        linkService.getTargetUrl(path);
    }

    @Test
    public void getLink() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.of(standaloneLink));
        Link link = linkService.getLink(LINK_ID);
        assertSame(standaloneLink, link);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getLink_withNonExistent() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.empty());
        linkService.getLink(LINK_ID);
    }

    @Test
    public void addLink() throws Exception {
        linkService.addLink(LONG_URL_BASE_S, UTM_PARAMETERS_FULL, USER_ID);

        StandaloneLink standaloneLink = captureSavedStandaloneLink();
        assertEquals(USER_ID, standaloneLink.getUserId());
        assertEquals(new URL(LONG_URL_BASE_S), standaloneLink.getBaseUrl());
        assertEquals(UTM_PARAMETERS_FULL, standaloneLink.getUtmParameters());
    }

    @Test
    public void updateLink() throws Exception {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.of(standaloneLink));
        linkService.updateLink(LINK_ID, LONG_URL_VALID_UTM_S, UTM_PARAMETERS_MINIMAL);

        StandaloneLink standaloneLink = captureSavedStandaloneLink();
        assertEquals(USER_ID, standaloneLink.getUserId());
        assertEquals(new URL(LONG_URL_VALID_UTM_S), standaloneLink.getTargetUrl());
        assertEquals(UTM_PARAMETERS_MINIMAL, standaloneLink.getUtmParameters());

    }

    @Test(expected = EntityNotFoundException.class)
    public void updateLink_withNonExistent() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.empty());
        linkService.updateLink(LINK_ID, LONG_URL_BASE_S, UTM_PARAMETERS_FULL);
    }

    @Test(expected = UnsupportedLinkOperationException.class)
    public void updateLink_withEmbeddedLink() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.of(embeddedLink));
        linkService.updateLink(LINK_ID, LONG_URL_BASE_S, UTM_PARAMETERS_FULL);
    }

    @Test
    public void listLinks() {
        Page<Link> linkPage = new PageImpl<>(links);
        when(linkRepository.findByUserId(USER_ID, unpaged())).thenReturn(linkPage);
        assertEquals(linkPage, linkService.listLinks(USER_ID, unpaged()));
    }

    @Test
    public void activateLink() throws ApplicationException {
        standaloneLink.markArchived();
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.of(standaloneLink));
        linkService.activateLink(LINK_ID);

        StandaloneLink standaloneLink = captureSavedStandaloneLink();
        assertEquals(ACTIVE, standaloneLink.getLinkStatus());
    }

    @Test(expected = EntityNotFoundException.class)
    public void activateLink_withNonExistent() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.empty());
        linkService.activateLink(LINK_ID);
    }

    @Test(expected = UnsupportedLinkOperationException.class)
    public void activateLink_withEmbeddedLink() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.of(embeddedLink));
        linkService.activateLink(LINK_ID);
    }

    @Test
    public void archiveLink() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.of(standaloneLink));
        linkService.archiveLink(LINK_ID);

        StandaloneLink standaloneLink = captureSavedStandaloneLink();
        assertEquals(ARCHIVED, standaloneLink.getLinkStatus());
    }

    @Test(expected = EntityNotFoundException.class)
    public void archiveLink_withNonExistent() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.empty());
        linkService.archiveLink(LINK_ID);
    }

    @Test(expected = UnsupportedLinkOperationException.class)
    public void archiveLink_withEmbeddedLink() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.of(embeddedLink));
        linkService.archiveLink(LINK_ID);
    }

    @Test
    public void addTag() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.of(standaloneLink));
        linkService.addTag(LINK_ID, "test");

    }

    @Test(expected = EntityNotFoundException.class)
    public void addTag_withNonExistent() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.empty());
        linkService.addTag(LINK_ID, "test");
    }

    @Test(expected = UnsupportedLinkOperationException.class)
    public void addTag_withEmbeddedLink() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.of(embeddedLink));
        linkService.addTag(LINK_ID, "test");
    }

    @Test
    public void removeTag() {
    }

    @Test(expected = EntityNotFoundException.class)
    public void removeTag_withNonExistent() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.empty());
        linkService.removeTag(LINK_ID, "test");
    }

    @Test(expected = UnsupportedLinkOperationException.class)
    public void removeTag_withEmbeddedLink() throws ApplicationException {
        when(linkRepository.findById(LINK_ID)).thenReturn(Optional.of(embeddedLink));
        linkService.removeTag(LINK_ID, "test");
    }

    private StandaloneLink captureSavedStandaloneLink() {
        ArgumentCaptor<StandaloneLink> standaloneLinkCaptor = ArgumentCaptor
                .forClass(StandaloneLink.class);
        verify(standaloneLinkRepository).save(standaloneLinkCaptor.capture());

        return standaloneLinkCaptor.getValue();
    }

}
