package io.relinkr.link.service;

import static io.relinkr.test.Mocks.LINK_ID;
import static io.relinkr.test.Mocks.LONG_URL_BASE_S;
import static io.relinkr.test.Mocks.LONG_URL_VALID_UTM_S;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.UTM_PARAMETERS_FULL;
import static io.relinkr.test.Mocks.UTM_PARAMETERS_MINIMAL;
import static io.relinkr.link.model.LinkStatus.ACTIVE;
import static io.relinkr.link.model.LinkStatus.ARCHIVED;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.data.domain.Pageable.unpaged;

import io.relinkr.test.Mocks;
import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import java.net.URI;
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

    protected Link link;

    private LinkId linkId;
    private List<Link> links;

    @Mock
    protected LinkRepository linkRepository;

    private LinkService linkService;

    @Before
    public void setUp() {
        link = Mocks.createLink();
        linkId = link.getId();
        links = singletonList(link);
        linkService = new LinkServiceImpl(linkRepository);
    }

    @Test
    public void getLink() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
        Link link = linkService.getLink(linkId);
        assertSame(this.link, link);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getLink_withNonExistent() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.empty());
        linkService.getLink(linkId);
    }

    @Test
    public void listLinks() {
        Page<Link> linkPage = new PageImpl<>(links);
        given(linkRepository.findByUserId(USER_ID, unpaged())).willReturn(linkPage);
        assertEquals(linkPage, linkService.listLinks(USER_ID, unpaged()));
    }

    @Test
    public void activateLink() throws ApplicationException {
        link.markArchived();
        given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
        linkService.activateLink(linkId);

        Link link = captureSavedLink();
        assertEquals(ACTIVE, link.getLinkStatus());
    }

    @Test(expected = EntityNotFoundException.class)
    public void activateLink_withNonExistent() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.empty());
        linkService.activateLink(linkId);
    }

    @Test
    public void archiveLink() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
        linkService.archiveLink(linkId);

        Link link = captureSavedLink();
        assertEquals(ARCHIVED, link.getLinkStatus());
    }

    @Test(expected = EntityNotFoundException.class)
    public void archiveLink_withNonExistent() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.empty());
        linkService.archiveLink(linkId);
    }

    @Test
    public void addTag() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
        linkService.addTag(linkId, "test");
    }

    @Test(expected = EntityNotFoundException.class)
    public void addTag_withNonExistent() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.empty());
        linkService.addTag(linkId, "test");
    }

    @Test
    public void removeTag() {
        given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
        linkService.removeTag(linkId, "test");
    }

    @Test(expected = EntityNotFoundException.class)
    public void removeTag_withNonExistent() throws ApplicationException {
        given(linkRepository.findById(linkId)).willReturn(Optional.empty());
        linkService.removeTag(linkId, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateLongUrl_withNull() throws ApplicationException {
        linkService.updateLongUrl(linkId, null);
    }

    @Test
    public void getTargetUrl() throws ApplicationException {
        String path = link.getPath();
        given(linkRepository.findByPath(path)).willReturn(Optional.of(link));
        URI targetUri = linkService.getTargetUrl(path);
        assertEquals(link.getTargetUrl(), targetUri);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getTargetUrl_withNonExistent() throws ApplicationException {
        String path = "bad path";
        given(linkRepository.findByPath(path)).willReturn(Optional.empty());
        linkService.getTargetUrl(path);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getTargetUrl_withArchived() throws ApplicationException {
        link.markArchived();
        String path = link.getPath();
        given(linkRepository.findByPath(path)).willReturn(Optional.of(link));
        linkService.getTargetUrl(path);
    }

    @Test
    public void addLink() {
        linkService.addLink(LONG_URL_BASE_S, UTM_PARAMETERS_FULL, USER_ID);

        Link link = captureSavedLink();

        assertEquals(USER_ID, link.getUserId());
        assertEquals(URI.create(LONG_URL_BASE_S), link.getLongUrl());
        assertEquals(UTM_PARAMETERS_FULL, link.getUtmParameters().get());
    }

    @Test
    public void updateUtmParameters() {
        given(linkRepository.findById(LINK_ID)).willReturn(Optional.of(link));
        linkService.updateUtmParameters(LINK_ID, UTM_PARAMETERS_MINIMAL);

        Link link = captureSavedLink();

        assertEquals(USER_ID, link.getUserId());
        assertEquals(URI.create(LONG_URL_VALID_UTM_S), link.getTargetUrl());
        assertEquals(UTM_PARAMETERS_MINIMAL, link.getUtmParameters().get());
    }

    private Link captureSavedLink() {
        ArgumentCaptor<Link> linkCaptor = ArgumentCaptor.forClass(Link.class);
        then(linkRepository).should().save(linkCaptor.capture());
        return linkCaptor.getValue();
    }

}
