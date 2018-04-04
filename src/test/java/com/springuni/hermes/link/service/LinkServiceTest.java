package com.springuni.hermes.link.service;

import static com.springuni.hermes.Mocks.LINK_ID;
import static com.springuni.hermes.Mocks.LONG_URL_BASE_S;
import static com.springuni.hermes.Mocks.LONG_URL_VALID_UTM_S;
import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_FULL;
import static com.springuni.hermes.Mocks.UTM_PARAMETERS_MINIMAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.springuni.hermes.Mocks;
import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.core.model.EntityNotFoundException;
import com.springuni.hermes.link.model.EmbeddedLink;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.StandaloneLink;
import com.springuni.hermes.link.model.UnsupportedLinkOperationException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LinkServiceTest extends
        AbstractLinkServiceTest<Long, Link, LinkRepository<Link>, LinkService> {

    @Mock
    private LinkRepository<Link> linkRepository;

    @Mock
    private StandaloneLinkRepository standaloneLinkRepository;

    private EmbeddedLink embeddedLink;
    private StandaloneLink standaloneLink;

    @Before
    public void setUp() throws Exception {
        linkService = new LinkServiceImpl(linkRepository, standaloneLinkRepository);
        embeddedLink = Mocks.createEmbeddedLink();
        standaloneLink = Mocks.createStandaloneLink();
        super.setUp();
    }

    @Override
    protected Long createLinkId() {
        return standaloneLink.getId();
    }

    @Override
    protected StandaloneLink createLink() {
        return standaloneLink;
    }

    @Override
    protected LinkService createLinkService() {
        return linkService;
    }

    @Override
    protected LinkRepository<Link> createLinkRepository() {
        return linkRepository;
    }

    @Override
    protected List<Link> createLinkList() {
        links = new ArrayList<>(2);
        links.add(embeddedLink);
        links.add(standaloneLink);
        return links;
    }

    @Test
    public void getTargetUrl() throws ApplicationException {
        String path = standaloneLink.getPath();
        given(linkRepository.findByPath(path)).willReturn(Optional.of(standaloneLink));
        URI targetUri = linkService.getTargetUrl(path);
        assertEquals(standaloneLink.getTargetUrl(), targetUri);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getTargetUrl_withNonExistent() throws ApplicationException {
        String path = "bad path";
        given(linkRepository.findByPath(path)).willReturn(Optional.empty());
        linkService.getTargetUrl(path);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getTargetUrl_withArchived() throws ApplicationException {
        standaloneLink.markArchived();
        String path = standaloneLink.getPath();
        given(linkRepository.findByPath(path)).willReturn(Optional.of(standaloneLink));
        linkService.getTargetUrl(path);
    }

    @Test
    public void addLink() {
        linkService.addLink(LONG_URL_BASE_S, UTM_PARAMETERS_FULL, USER_ID);

        Link standaloneLink = captureSavedStandaloneLink();

        assertEquals(USER_ID, standaloneLink.getUserId());
        assertEquals(URI.create(LONG_URL_BASE_S), standaloneLink.getLongUrl());
        assertEquals(UTM_PARAMETERS_FULL, standaloneLink.getUtmParameters().get());
    }

    @Test
    public void updateUtmParameters() {
        given(linkRepository.findById(LINK_ID)).willReturn(Optional.of(standaloneLink));
        linkService.updateUtmParameters(LINK_ID, UTM_PARAMETERS_MINIMAL);

        StandaloneLink standaloneLink = captureSavedStandaloneLink();

        assertEquals(USER_ID, standaloneLink.getUserId());
        assertEquals(URI.create(LONG_URL_VALID_UTM_S), standaloneLink.getTargetUrl());
        assertEquals(UTM_PARAMETERS_MINIMAL, standaloneLink.getUtmParameters().get());
    }

    @Test(expected = UnsupportedLinkOperationException.class)
    public void updateUtmParameters_withEmbeddedLink() throws ApplicationException {
        given(linkRepository.findById(LINK_ID)).willReturn(Optional.of(embeddedLink));
        linkService.updateUtmParameters(LINK_ID, UTM_PARAMETERS_FULL);
    }

    @Test(expected = UnsupportedLinkOperationException.class)
    public void activateLink_withEmbeddedLink() throws ApplicationException {
        given(linkRepository.findById(LINK_ID)).willReturn(Optional.of(embeddedLink));
        linkService.activateLink(LINK_ID);
    }

    @Test(expected = UnsupportedLinkOperationException.class)
    public void archiveLink_withEmbeddedLink() throws ApplicationException {
        given(linkRepository.findById(LINK_ID)).willReturn(Optional.of(embeddedLink));
        linkService.archiveLink(LINK_ID);
    }

    @Test(expected = UnsupportedLinkOperationException.class)
    public void addTag_withEmbeddedLink() throws ApplicationException {
        given(linkRepository.findById(LINK_ID)).willReturn(Optional.of(embeddedLink));
        linkService.addTag(LINK_ID, "test");
    }

    @Test(expected = UnsupportedLinkOperationException.class)
    public void removeTag_withEmbeddedLink() throws ApplicationException {
        given(linkRepository.findById(LINK_ID)).willReturn(Optional.of(embeddedLink));
        linkService.removeTag(LINK_ID, "test");
    }

    private StandaloneLink captureSavedStandaloneLink() {
        ArgumentCaptor<StandaloneLink> standaloneLinkCaptor = ArgumentCaptor
                .forClass(StandaloneLink.class);
        then(standaloneLinkRepository).should().save(standaloneLinkCaptor.capture());
        return standaloneLinkCaptor.getValue();
    }

}
