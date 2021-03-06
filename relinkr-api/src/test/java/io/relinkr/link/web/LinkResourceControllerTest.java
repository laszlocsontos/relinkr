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

package io.relinkr.link.web;

import static io.relinkr.link.model.LinkStatus.ACTIVE;
import static io.relinkr.link.model.LinkStatus.ARCHIVED;
import static io.relinkr.test.Mocks.PAGEABLE;
import static io.relinkr.test.Mocks.TAG_A;
import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.createLink;
import static java.util.Arrays.asList;
import static javax.persistence.LockModeType.NONE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import io.relinkr.link.model.UtmParameters;
import io.relinkr.link.service.LinkService;
import io.relinkr.link.web.LinkResourceControllerTest.TestConfig;
import io.relinkr.test.security.AbstractResourceControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = LinkResourceController.class)
public class LinkResourceControllerTest extends AbstractResourceControllerTest {

  @Autowired
  private LinkResourceAssembler linkResourceAssembler;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private LinkService linkService;

  private Link link;

  @Before
  public void setUp() {
    link = createLink();
    given(entityManager.find(Link.class, link.getUserId(), NONE)).willReturn(link);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenLinkOwnedByCurrentUser_whenGetLink_thenOk() throws Exception {
    given(linkService.getLink(link.getId())).willReturn(link);

    ResultActions resultActions = mockMvc
        .perform(get("/v1/links/{linkId}", link.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
        .andDo(print());

    assertLink(resultActions);
  }

  @Test
  @WithMockUser(username = "0") // USER_ID_ZERO
  public void givenLinkOwnedByOtherUser_whenGetLink_thenForbidden() throws Exception {
    given(linkService.getLink(link.getId())).willReturn(link);

    ResultActions resultActions = mockMvc
        .perform(get("/v1/links/{linkId}", link.getId()))
        .andExpect(status().isForbidden())
        .andDo(print());

    assertError(403, "Access is denied", resultActions);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenAuthenticatedUser_whenAddLink_thenAddedAndOwnedByUser() throws Exception {
    LinkResource linkResource = LinkResource.of(
        link.getLongUrl().toString(),
        link.getUtmParameters().orElse(null)
    );

    String longUrl = linkResource.getLongUrl();
    UtmParameters utmParameters = linkResource.getUtmParameters().orElse(null);

    given(linkService.addLink(longUrl, utmParameters, USER_ID)).willReturn(link);

    ResultActions resultActions = mockMvc
        .perform(post("/v1/links").contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linkResource)))
        .andExpect(status().isOk())
        .andDo(print());

    then(linkService).should().addLink(longUrl, utmParameters, USER_ID);

    assertLink(resultActions);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenLinkOwnedByCurrentUserWithLongUrl_whenReplaceLink_thenReplaced()
      throws Exception {
    String longUrl = link.getLongUrl().toString();
    UtmParameters utmParameters = link.getUtmParameters().get();
    LinkResource linkResource = LinkResource.of(longUrl, utmParameters);

    LinkId linkId = link.getId();

    given(linkService.updateLongUrl(linkId, longUrl, utmParameters)).willReturn(link);

    ResultActions resultActions = mockMvc.perform(
        put("/v1/links/{linkId}", linkId).contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linkResource)))
        .andExpect(status().isOk())
        .andDo(print());

    then(linkService).should().updateLongUrl(linkId, longUrl, utmParameters);

    assertLink(resultActions);

    then(linkService).should().updateLongUrl(linkId, longUrl, utmParameters);
  }

  @Test
  @WithMockUser(username = "0") // USER_ID_ZERO
  public void givenLinkOwnedByOtherUserWithLongUrl_whenReplaceLink_thenForbidden()
      throws Exception {

    String longUrl = link.getLongUrl().toString();
    UtmParameters utmParameters = link.getUtmParameters().get();
    LinkResource linkResource = LinkResource.of(longUrl, utmParameters);

    LinkId linkId = link.getId();

    given(linkService.updateLongUrl(linkId, longUrl, utmParameters)).willReturn(link);

    ResultActions resultActions = mockMvc.perform(
        put("/v1/links/{linkId}", linkId).contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linkResource)))
        .andDo(print());

    then(linkService).shouldHaveZeroInteractions();

    assertError(403, "Access is denied", resultActions);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenLinkOwnedByCurrentUserWithoutLongUrl_whenReplaceLink_thenBadRequest()
      throws Exception {

    UtmParameters utmParameters = link.getUtmParameters().get();
    LinkResource linkResource = LinkResource.of(null, utmParameters);

    LinkId linkId = link.getId();

    ResultActions resultActions = mockMvc.perform(
        put("/v1/links/{linkId}", linkId).contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linkResource)))
        .andExpect(status().isBadRequest())
        .andDo(print());

    then(linkService).shouldHaveZeroInteractions();

    assertError(400, "Validation failed", resultActions);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenLinkOwnedByCurrentUserWithLongUrl_whenUpdateLink_thenUpdated() throws Exception {
    String longUrl = link.getLongUrl().toString();
    LinkResource linkResource = LinkResource.of(longUrl);

    LinkId linkId = link.getId();

    given(linkService.updateLongUrl(linkId, longUrl)).willReturn(link);

    ResultActions resultActions = mockMvc.perform(
        patch("/v1/links/{linkId}", linkId).contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linkResource)))
        .andExpect(status().isOk())
        .andDo(print());

    assertLink(resultActions);

    then(linkService).should().updateLongUrl(linkId, longUrl);
  }

  @Test
  @WithMockUser(username = "0") // USER_ID_ZERO
  public void givenLinkOwnedByOtherUserWithLongUrl_whenUpdateLink_thenForbidden() throws Exception {
    String longUrl = link.getLongUrl().toString();
    LinkResource linkResource = LinkResource.of(longUrl);

    LinkId linkId = link.getId();

    ResultActions resultActions = mockMvc.perform(
        patch("/v1/links/{linkId}", linkId).contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linkResource)))
        .andDo(print());

    then(linkService).shouldHaveZeroInteractions();

    assertError(403, "Access is denied", resultActions);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenLinkOwnedByCurrentUserWithUtmParameters_whenUpdateLink_thenUpdated()
      throws Exception {

    UtmParameters utmParameters = link.getUtmParameters().orElse(null);
    LinkResource linkResource = LinkResource.of(utmParameters);

    LinkId linkId = link.getId();

    given(linkService.updateUtmParameters(linkId, utmParameters)).willReturn(link);

    ResultActions resultActions = mockMvc.perform(
        patch("/v1/links/{linkId}", linkId).contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linkResource)))
        .andExpect(status().isOk())
        .andDo(print());

    assertLink(resultActions);

    then(linkService).should().updateUtmParameters(linkId, utmParameters);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenLinkOwnedByCurrentUserWithoutUtmParameters_whenUpdateLink_BadRequest()
      throws Exception {

    LinkResource linkResource = new LinkResource(null, null, null, null, null, null, null);

    LinkId linkId = link.getId();

    ResultActions resultActions = mockMvc.perform(
        patch("/v1/links/{linkId}", linkId).contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linkResource)))
        .andExpect(status().isBadRequest())
        .andDo(print());

    assertError(400, "Validation failed", resultActions);

    then(linkService).shouldHaveZeroInteractions();
  }


  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenAuthenticatedUser_whenFetchLinks_thenFetched() throws Exception {
    given(linkService.fetchLinks(USER_ID, PAGEABLE))
        .willReturn(new PageImpl<>(asList(link), PAGEABLE, 1));

    ResultActions resultActions = mockMvc.perform(get("/v1/links")
        .param("page", String.valueOf(PAGEABLE.getPageNumber()))
        .param("size", String.valueOf(PAGEABLE.getPageSize())))
        .andExpect(status().isOk())
        .andDo(print());

    assertLink("_embedded.links[0]", resultActions);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenLinkOwnedByCurrentUserWithActive_whenUpdateLinkStatus_thenActivated()
      throws Exception {

    mockMvc.perform(put("/v1/links/{linkId}/linkStatuses/{linkStatus}", link.getId(),
        ACTIVE.name()))
        .andExpect(status().isOk())
        .andDo(print());

    then(linkService).should().activateLink(link.getId());
    then(linkService).shouldHaveNoMoreInteractions();
  }

  @Test
  @WithMockUser(username = "0") // USER_ID_ZERO
  public void givenLinkOwnedByOtherUserWithActive_whenUpdateLinkStatus_thenForbidden()
      throws Exception {

    ResultActions resultActions = mockMvc.perform(
        put("/v1/links/{linkId}/linkStatuses/{linkStatus}", link.getId(), ACTIVE.name()))
        .andDo(print());

    then(linkService).shouldHaveZeroInteractions();

    assertError(403, "Access is denied", resultActions);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenLinkOwnedByCurrentUserWithArchive_whenUpdateLinkStatus_thenArchived()
      throws Exception {

    mockMvc.perform(put("/v1/links/{linkId}/linkStatuses/{linkStatus}", link.getId(),
        ARCHIVED.name()))
        .andExpect(status().isOk())
        .andDo(print());

    then(linkService).should().archiveLink(link.getId());
    then(linkService).shouldHaveNoMoreInteractions();
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenLinkOwnedByCurrentUser_whenAddTag_thenTagAdded() throws Exception {
    mockMvc.perform(post("/v1/links/{linkId}/tags/{tagName}", link.getId(),
        TAG_A.getTagName()))
        .andExpect(status().isOk())
        .andDo(print());

    then(linkService).should().addTag(link.getId(), TAG_A.getTagName());
    then(linkService).shouldHaveNoMoreInteractions();
  }

  @Test
  @WithMockUser(username = "0") // USER_ID_ZERO
  public void givenLinkOwnedByOtherUser_whenAddTag_thenForbidden() throws Exception {
    ResultActions resultActions = mockMvc.perform(
        post("/v1/links/{linkId}/tags/{tagName}", link.getId(), TAG_A.getTagName()))
        .andDo(print());

    then(linkService).shouldHaveZeroInteractions();

    assertError(403, "Access is denied", resultActions);
  }

  @Test
  @WithMockUser(username = "1") // USER_ID
  public void givenLinkOwnedByCurrentUser_whenRemoveTag_thenRemoved() throws Exception {
    mockMvc.perform(delete("/v1/links/{linkId}/tags/{tagName}", link.getId(),
        TAG_A.getTagName()))
        .andExpect(status().isOk())
        .andDo(print());

    then(linkService).should().removeTag(link.getId(), TAG_A.getTagName());
    then(linkService).shouldHaveNoMoreInteractions();
  }

  @Test
  @WithMockUser(username = "0") // USER_ID_ZERO
  public void givenLinkOwnedByOtherUser_whenRemoveTag_thenForbidden() throws Exception {
    ResultActions resultActions = mockMvc.perform(
        delete("/v1/links/{linkId}/tags/{tagName}", link.getId(), TAG_A.getTagName()))
        .andDo(print());

    then(linkService).shouldHaveZeroInteractions();

    assertError(403, "Access is denied", resultActions);
  }

  private void assertLink(ResultActions resultActions) throws Exception {
    assertLink("$", resultActions);
  }

  private void assertLink(String path, ResultActions resultActions) throws Exception {
    String shortLinkScheme = linkResourceAssembler.getShortLinkScheme().orElse("http");
    String shortLinkDomain = linkResourceAssembler.getShortLinkDomain().orElse("localhost");

    resultActions
        .andExpect(jsonPath(path + ".id", is(link.getId().toString())))
        .andExpect(jsonPath(path + ".longUrl", is(link.getLongUrl().toString())))
        .andExpect(
            jsonPath(path + ".targetUrl", is(link.getTargetUrl().toString())))
        .andExpect(jsonPath(path + ".tags", hasSize(link.getTags().size())))
        .andExpect(jsonPath(path + ".linkStatus", is(ACTIVE.name())))
        .andExpect(jsonPath(path + ".path", is(link.getPath())))
        .andExpect(jsonPath(path + ".utmParameters.utmSource",
            is(link.getUtmParameters().get().getUtmSource())))
        .andExpect(jsonPath(path + ".utmParameters.utmMedium",
            is(link.getUtmParameters().get().getUtmMedium())))
        .andExpect(jsonPath(path + ".utmParameters.utmCampaign",
            is(link.getUtmParameters().get().getUtmCampaign())))
        .andExpect(jsonPath(path + ".utmParameters.utmTerm",
            is(link.getUtmParameters().get().getUtmTerm().orElse(null))))
        .andExpect(jsonPath(path + ".utmParameters.utmContent",
            is(link.getUtmParameters().get().getUtmContent().orElse(null))))
        .andExpect(jsonPath(path + "._links.self.href",
            is("http://localhost/v1/links/" + link.getId())))
        .andExpect(jsonPath(path + "._links.userLinkStatuses.href",
            is("http://localhost/v1/links/" + link.getId()
                + "/linkStatuses/ARCHIVED")))
        .andExpect(jsonPath(path + "._links.shortLink.href",
            is(shortLinkScheme + "://" + shortLinkDomain + "/" + link.getPath())));
  }

  @TestConfiguration
  @Import(AbstractResourceControllerTest.TestConfig.class)
  public static class TestConfig {

    @Bean
    LinkResourceAssembler linkResourceAssembler(Environment environment) {
      return new LinkResourceAssembler(environment);
    }

  }

}
