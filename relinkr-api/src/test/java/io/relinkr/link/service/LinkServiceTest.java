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

package io.relinkr.link.service;

import static io.relinkr.link.model.LinkStatus.ACTIVE;
import static io.relinkr.link.model.LinkStatus.ARCHIVED;
import static io.relinkr.test.Mocks.EMAIL_ADDRESS;
import static io.relinkr.test.Mocks.LINK_ID;
import static io.relinkr.test.Mocks.LONG_URL_BASE_S;
import static io.relinkr.test.Mocks.LONG_URL_VALID_UTM_S;
import static io.relinkr.test.Mocks.UTM_PARAMETERS_FULL;
import static io.relinkr.test.Mocks.UTM_PARAMETERS_MINIMAL;
import static io.relinkr.test.Mocks.createLink;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.data.domain.Pageable.unpaged;

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

  @Mock
  protected LinkRepository linkRepository;

  private LinkId linkId;
  private List<Link> links;
  private LinkService linkService;

  @Before
  public void setUp() {
    link = createLink();
    linkId = link.getId();
    links = singletonList(link);
    linkService = new LinkServiceImpl(linkRepository);
  }

  @Test
  public void givenExistentLink_whenGetLink_thenFound() {
    given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
    Link link = linkService.getLink(linkId);
    assertSame(this.link, link);
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentLink_whenGetLink_thenEntityNotFoundException() {
    given(linkRepository.findById(linkId)).willReturn(Optional.empty());
    linkService.getLink(linkId);
  }

  @Test
  public void givenOwnerAndPageable_whenFetchLinks_thenPageReturned() {
    Page<Link> linkPage = new PageImpl<>(links);
    given(linkRepository.findByOwner(EMAIL_ADDRESS, unpaged())).willReturn(linkPage);
    assertEquals(linkPage, linkService.fetchLinks(EMAIL_ADDRESS, unpaged()));
  }

  @Test
  public void givenArchivedLink_whenActivateLink_thenSavedLinkIsActivated() {
    link.markArchived();
    given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
    linkService.activateLink(linkId);

    Link link = captureSavedLink();
    assertEquals(ACTIVE, link.getLinkStatus());
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentLink_whenActivateLink_thenEntityNotFoundException() {
    given(linkRepository.findById(linkId)).willReturn(Optional.empty());
    linkService.activateLink(linkId);
  }

  @Test
  public void givenActiveLink_whenArchiveLink_thenSavedLinkIsArchived() {
    // Sanity check
    assertEquals(ACTIVE, link.getLinkStatus());

    given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
    linkService.archiveLink(linkId);

    Link link = captureSavedLink();
    assertEquals(ARCHIVED, link.getLinkStatus());
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentLink_whenArchiveLink_thenEntityNotFoundException() {
    given(linkRepository.findById(linkId)).willReturn(Optional.empty());
    linkService.archiveLink(linkId);
  }

  @Test
  public void givenExistentLink_whenAddTag_thenTagAdded() {
    given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
    linkService.addTag(linkId, "test");
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentLink_whenAddTag_thenEntityNotFoundException() {
    given(linkRepository.findById(linkId)).willReturn(Optional.empty());
    linkService.addTag(linkId, "test");
  }

  @Test
  public void givenExistentLink_whenRemoveTag_thenTagRemoved() {
    given(linkRepository.findById(linkId)).willReturn(Optional.of(link));
    linkService.removeTag(linkId, "test");
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentLink_whenRemoveTag_thenEntityNotFoundException() {
    given(linkRepository.findById(linkId)).willReturn(Optional.empty());
    linkService.removeTag(linkId, "test");
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullLongUrl_whenUpdateLongUrl_thenIllegalArgumentException() {
    linkService.updateLongUrl(linkId, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenEmptyLongUrl_whenUpdateLongUrl_thenIllegalArgumentException() {
    linkService.updateLongUrl(linkId, "");
  }

  @Test
  public void givenExistentLink_whenGetTargetUrl_thenReturned() {
    String path = link.getPath();
    given(linkRepository.findByPath(path)).willReturn(Optional.of(link));
    URI targetUri = linkService.getTargetUrl(path);
    assertEquals(link.getTargetUrl(), targetUri);
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentLink_whenGetTargetUrl_thenEntityNotFoundException() {
    String path = "bad path";
    given(linkRepository.findByPath(path)).willReturn(Optional.empty());
    linkService.getTargetUrl(path);
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenArchivedLink_GetTargetUrl_thenEntityNotFoundException() {
    link.markArchived();
    String path = link.getPath();
    given(linkRepository.findByPath(path)).willReturn(Optional.of(link));
    linkService.getTargetUrl(path);
  }

  @Test
  public void givenBaseUrlWithUtmParameters_whenAddLink_thenLinkAdded() {
    linkService.addLink(LONG_URL_BASE_S, UTM_PARAMETERS_FULL, EMAIL_ADDRESS);

    Link link = captureSavedLink();

    assertEquals(ACTIVE, link.getLinkStatus());
    assertEquals(EMAIL_ADDRESS, link.getOwner());
    assertEquals(URI.create(LONG_URL_BASE_S), link.getLongUrl());
    assertEquals(UTM_PARAMETERS_FULL, link.getUtmParameters().get());
  }

  @Test
  public void givenBaseUrlWithoutUtmParameters_whenAddLink_thenLinkAdded() {
    linkService.addLink(LONG_URL_BASE_S, null, EMAIL_ADDRESS);

    Link link = captureSavedLink();

    assertEquals(ACTIVE, link.getLinkStatus());
    assertEquals(EMAIL_ADDRESS, link.getOwner());
    assertEquals(URI.create(LONG_URL_BASE_S), link.getLongUrl());
    assertFalse(link.getUtmParameters().isPresent());
  }

  @Test
  public void givenExistentLink_whenUpdateUtmParameters_thenUpdated() {
    given(linkRepository.findById(LINK_ID)).willReturn(Optional.of(link));
    linkService.updateUtmParameters(LINK_ID, UTM_PARAMETERS_MINIMAL);

    Link link = captureSavedLink();

    assertEquals(EMAIL_ADDRESS, link.getOwner());
    assertEquals(URI.create(LONG_URL_VALID_UTM_S), link.getTargetUrl());
    assertEquals(UTM_PARAMETERS_MINIMAL, link.getUtmParameters().get());
  }

  @Test(expected = EntityNotFoundException.class)
  public void givenNonExistentLink_whenUpdateUtmParameters_thenEntityNotFoundException() {
    given(linkRepository.findById(LINK_ID)).willReturn(Optional.empty());
    linkService.updateUtmParameters(LINK_ID, UTM_PARAMETERS_MINIMAL);
  }

  private Link captureSavedLink() {
    ArgumentCaptor<Link> linkCaptor = ArgumentCaptor.forClass(Link.class);
    then(linkRepository).should().save(linkCaptor.capture());
    return linkCaptor.getValue();
  }

}
