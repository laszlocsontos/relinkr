package com.springuni.hermes.link.web;

import static com.springuni.hermes.Mocks.PAGEABLE;
import static com.springuni.hermes.Mocks.TAG_A;
import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.createStandaloneLink;
import static com.springuni.hermes.link.model.LinkStatus.ACTIVE;
import static com.springuni.hermes.link.model.LinkStatus.ARCHIVED;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
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
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.link.model.StandaloneLink;
import com.springuni.hermes.link.service.LinkService;
import com.springuni.hermes.link.web.LinkResourceControllerTest.TestConfig;
import com.springuni.hermes.utm.model.UtmParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = LinkResourceController.class, secure = false)
public class LinkResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LinkService linkService;

    private StandaloneLink standaloneLink;

    @Before
    public void setUp() throws Exception {
        standaloneLink = createStandaloneLink();
    }

    @Test
    public void getLink() throws Exception {
        given(linkService.getLink(standaloneLink.getId())).willReturn(standaloneLink);

        ResultActions resultActions = mockMvc
                .perform(get("/api/links/{linkId}", standaloneLink.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andDo(print());

        assertStandaloneLink(resultActions);
    }

    @Test
    public void addLink() throws Exception {
        LinkResource linkResource = new LinkResource(
                standaloneLink.getLongUrl().toString(),
                standaloneLink.getUtmParameters().orElse(null)
        );

        given(
                linkService.addLink(
                        linkResource.getLongUrl(),
                        linkResource.getUtmParameters().orElse(null),
                        USER_ID
                )
        ).willReturn(standaloneLink);

        ResultActions resultActions = mockMvc
                .perform(post("/api/links").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isOk())
                .andDo(print());

        assertStandaloneLink(resultActions);
    }

    @Test
    public void replaceLink() throws Exception {
        String longUrl = standaloneLink.getLongUrl().toString();
        UtmParameters utmParameters = standaloneLink.getUtmParameters().get();
        LinkResource linkResource = new LinkResource(longUrl, utmParameters);

        LinkId linkId = standaloneLink.getId();

        given(
                linkService.updateLongUrl(
                        linkId,
                        longUrl,
                        utmParameters
                )
        ).willReturn(standaloneLink);

        ResultActions resultActions = mockMvc.perform(
                put("/api/links/{linkId}", linkId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isOk())
                .andDo(print());

        assertStandaloneLink(resultActions);

        then(linkService).should().updateLongUrl(linkId, longUrl, utmParameters);
    }

    @Test
    public void replaceLink_withoutLongUrl() throws Exception {
        UtmParameters utmParameters = standaloneLink.getUtmParameters().get();
        LinkResource linkResource = new LinkResource(null, utmParameters);

        LinkId linkId = standaloneLink.getId();

        ResultActions resultActions = mockMvc.perform(
                put("/api/links/{linkId}", linkId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        assertError(400, "Validation failed", resultActions);

        then(linkService).shouldHaveZeroInteractions();
    }

    @Test
    public void updateLink_withLongUrl() throws Exception {
        String longUrl = standaloneLink.getLongUrl().toString();
        LinkResource linkResource = new LinkResource(longUrl);

        LinkId linkId = standaloneLink.getId();

        given(
                linkService.updateLongUrl(
                        linkId,
                        longUrl
                )
        ).willReturn(standaloneLink);

        ResultActions resultActions = mockMvc.perform(
                patch("/api/links/{linkId}", linkId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isOk())
                .andDo(print());

        assertStandaloneLink(resultActions);

        then(linkService).should().updateLongUrl(linkId, longUrl);
    }

    @Test
    public void updateLink_withUtmParameters() throws Exception {
        UtmParameters utmParameters = standaloneLink.getUtmParameters().orElse(null);
        LinkResource linkResource = new LinkResource(utmParameters);

        LinkId linkId = standaloneLink.getId();

        given(
                linkService.updateUtmParameters(
                        linkId,
                        utmParameters
                )
        ).willReturn(standaloneLink);

        ResultActions resultActions = mockMvc.perform(
                patch("/api/links/{linkId}", linkId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isOk())
                .andDo(print());

        assertStandaloneLink(resultActions);

        then(linkService).should().updateUtmParameters(linkId, utmParameters);
    }

    @Test
    public void updateLink_withoutParameters() throws Exception {
        LinkResource linkResource = new LinkResource();

        LinkId linkId = standaloneLink.getId();

        ResultActions resultActions = mockMvc.perform(
                patch("/api/links/{linkId}", linkId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        assertError(400, "Validation failed", resultActions);

        then(linkService).shouldHaveZeroInteractions();
    }


    @Test
    public void listLinks() throws Exception {
        given(linkService.listLinks(USER_ID, PAGEABLE))
                .willReturn(new PageImpl<>(asList(standaloneLink), PAGEABLE, 1));

        ResultActions resultActions = mockMvc.perform(get("/api/links")
                .param("page", String.valueOf(PAGEABLE.getPageNumber()))
                .param("size", String.valueOf(PAGEABLE.getPageSize())))
                .andExpect(status().isOk())
                .andDo(print());

        assertStandaloneLink("_embedded.links[0]", resultActions);
    }

    @Test
    public void updateLinkStatus_withActive() throws Exception {
        mockMvc.perform(put("/api/links/{linkId}/linkStatuses/{linkStatus}", standaloneLink.getId(),
                ACTIVE.name()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().activateLink(standaloneLink.getId());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void updateLinkStatus_withArchived() throws Exception {
        mockMvc.perform(put("/api/links/{linkId}/linkStatuses/{linkStatus}", standaloneLink.getId(),
                ARCHIVED.name()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().archiveLink(standaloneLink.getId());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void addTag() throws Exception {
        mockMvc.perform(post("/api/links/{linkId}/tags/{tagName}", standaloneLink.getId(),
                TAG_A.getTagName()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().addTag(standaloneLink.getId(), TAG_A.getTagName());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void removeTag() throws Exception {
        mockMvc.perform(delete("/api/links/{linkId}/tags/{tagName}", standaloneLink.getId(),
                TAG_A.getTagName()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().removeTag(standaloneLink.getId(), TAG_A.getTagName());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    private void assertError(int statusCode, String detailMessage, ResultActions resultActions)
            throws Exception {

        resultActions
                .andExpect(jsonPath("$.statusCode", is(statusCode)))
                .andExpect(jsonPath("$.detailMessage", startsWith(detailMessage)));
    }

    private void assertStandaloneLink(ResultActions resultActions) throws Exception {
        assertStandaloneLink("$", resultActions);
    }

    private void assertStandaloneLink(String path, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(jsonPath(path + ".id", is(standaloneLink.getId().toString())))
                .andExpect(jsonPath(path + ".longUrl", is(standaloneLink.getLongUrl().toString())))
                .andExpect(
                        jsonPath(path + ".targetUrl", is(standaloneLink.getTargetUrl().toString())))
                .andExpect(jsonPath(path + ".tags", hasSize(standaloneLink.getTags().size())))
                .andExpect(jsonPath(path + ".linkStatus", is(ACTIVE.name())))
                .andExpect(jsonPath(path + ".path", is(standaloneLink.getPath())))
                .andExpect(jsonPath(path + ".utmParameters.utmSource",
                        is(standaloneLink.getUtmParameters().get().getUtmSource())))
                .andExpect(jsonPath(path + ".utmParameters.utmMedium",
                        is(standaloneLink.getUtmParameters().get().getUtmMedium())))
                .andExpect(jsonPath(path + ".utmParameters.utmCampaign",
                        is(standaloneLink.getUtmParameters().get().getUtmCampaign())))
                .andExpect(jsonPath(path + ".utmParameters.utmTerm",
                        is(standaloneLink.getUtmParameters().get().getUtmTerm().orElse(null))))
                .andExpect(jsonPath(path + ".utmParameters.utmContent",
                        is(standaloneLink.getUtmParameters().get().getUtmContent().orElse(null))))
                .andExpect(jsonPath(path + "._links.self.href",
                        is("http://localhost/api/links/" + standaloneLink.getId())))
                .andExpect(jsonPath(path + "._links.userLinkStatuses.href",
                        is("http://localhost/api/links/" + standaloneLink.getId()
                                + "/linkStatuses/ARCHIVED")))
                .andExpect(jsonPath(path + "._links.shortLink.href",
                        is("http://localhost/" + standaloneLink.getPath())));
    }

    @TestConfiguration
    @EnableHypermediaSupport(type = HAL)
    @EnableSpringDataWebSupport
    public static class TestConfig {

        @Bean
        LinkResourceAssembler linkResourceAssembler() {
            return new LinkResourceAssembler();
        }

    }

}