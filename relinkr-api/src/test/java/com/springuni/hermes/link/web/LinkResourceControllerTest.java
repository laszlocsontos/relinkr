package com.springuni.hermes.link.web;

import static com.springuni.hermes.test.Mocks.PAGEABLE;
import static com.springuni.hermes.test.Mocks.TAG_A;
import static com.springuni.hermes.test.Mocks.USER_ID;
import static com.springuni.hermes.test.Mocks.createLink;
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
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.link.model.UtmParameters;
import com.springuni.hermes.link.service.LinkService;
import com.springuni.hermes.link.web.LinkResourceControllerTest.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.test.context.support.WithMockUser;
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

    @Autowired
    LinkResourceAssembler linkResourceAssembler;

    private Link link;

    @Before
    public void setUp() throws Exception {
        link = createLink();
    }

    @Test
    public void getLink() throws Exception {
        given(linkService.getLink(link.getId())).willReturn(link);

        ResultActions resultActions = mockMvc
                .perform(get("/api/links/{linkId}", link.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andDo(print());

        assertLink(resultActions);
    }

    @Test
    @WithMockUser(username = "1") // USER_ID
    public void addLink() throws Exception {
        LinkResource linkResource = new LinkResource(
                link.getLongUrl().toString(),
                link.getUtmParameters().orElse(null)
        );

        given(
                linkService.addLink(
                        linkResource.getLongUrl(),
                        linkResource.getUtmParameters().orElse(null),
                        USER_ID
                )
        ).willReturn(link);

        ResultActions resultActions = mockMvc
                .perform(post("/api/links").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isOk())
                .andDo(print());

        assertLink(resultActions);
    }

    @Test
    public void replaceLink() throws Exception {
        String longUrl = link.getLongUrl().toString();
        UtmParameters utmParameters = link.getUtmParameters().get();
        LinkResource linkResource = new LinkResource(longUrl, utmParameters);

        LinkId linkId = link.getId();

        given(
                linkService.updateLongUrl(
                        linkId,
                        longUrl,
                        utmParameters
                )
        ).willReturn(link);

        ResultActions resultActions = mockMvc.perform(
                put("/api/links/{linkId}", linkId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isOk())
                .andDo(print());

        assertLink(resultActions);

        then(linkService).should().updateLongUrl(linkId, longUrl, utmParameters);
    }

    @Test
    public void replaceLink_withoutLongUrl() throws Exception {
        UtmParameters utmParameters = link.getUtmParameters().get();
        LinkResource linkResource = new LinkResource(null, utmParameters);

        LinkId linkId = link.getId();

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
        String longUrl = link.getLongUrl().toString();
        LinkResource linkResource = new LinkResource(longUrl);

        LinkId linkId = link.getId();

        given(
                linkService.updateLongUrl(
                        linkId,
                        longUrl
                )
        ).willReturn(link);

        ResultActions resultActions = mockMvc.perform(
                patch("/api/links/{linkId}", linkId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isOk())
                .andDo(print());

        assertLink(resultActions);

        then(linkService).should().updateLongUrl(linkId, longUrl);
    }

    @Test
    public void updateLink_withUtmParameters() throws Exception {
        UtmParameters utmParameters = link.getUtmParameters().orElse(null);
        LinkResource linkResource = new LinkResource(utmParameters);

        LinkId linkId = link.getId();

        given(
                linkService.updateUtmParameters(
                        linkId,
                        utmParameters
                )
        ).willReturn(link);

        ResultActions resultActions = mockMvc.perform(
                patch("/api/links/{linkId}", linkId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isOk())
                .andDo(print());

        assertLink(resultActions);

        then(linkService).should().updateUtmParameters(linkId, utmParameters);
    }

    @Test
    public void updateLink_withoutParameters() throws Exception {
        LinkResource linkResource = new LinkResource();

        LinkId linkId = link.getId();

        ResultActions resultActions = mockMvc.perform(
                patch("/api/links/{linkId}", linkId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        assertError(400, "Validation failed", resultActions);

        then(linkService).shouldHaveZeroInteractions();
    }


    @Test
    @WithMockUser(username = "1") // USER_ID
    public void listLinks() throws Exception {
        given(linkService.listLinks(USER_ID, PAGEABLE))
                .willReturn(new PageImpl<>(asList(link), PAGEABLE, 1));

        ResultActions resultActions = mockMvc.perform(get("/api/links")
                .param("page", String.valueOf(PAGEABLE.getPageNumber()))
                .param("size", String.valueOf(PAGEABLE.getPageSize())))
                .andExpect(status().isOk())
                .andDo(print());

        assertLink("_embedded.links[0]", resultActions);
    }

    @Test
    public void updateLinkStatus_withActive() throws Exception {
        mockMvc.perform(put("/api/links/{linkId}/linkStatuses/{linkStatus}", link.getId(),
                ACTIVE.name()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().activateLink(link.getId());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void updateLinkStatus_withArchived() throws Exception {
        mockMvc.perform(put("/api/links/{linkId}/linkStatuses/{linkStatus}", link.getId(),
                ARCHIVED.name()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().archiveLink(link.getId());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void addTag() throws Exception {
        mockMvc.perform(post("/api/links/{linkId}/tags/{tagName}", link.getId(),
                TAG_A.getTagName()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().addTag(link.getId(), TAG_A.getTagName());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void removeTag() throws Exception {
        mockMvc.perform(delete("/api/links/{linkId}/tags/{tagName}", link.getId(),
                TAG_A.getTagName()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().removeTag(link.getId(), TAG_A.getTagName());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    private void assertError(int statusCode, String detailMessage, ResultActions resultActions)
            throws Exception {

        resultActions
                .andExpect(jsonPath("$.statusCode", is(statusCode)))
                .andExpect(jsonPath("$.detailMessage", startsWith(detailMessage)));
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
                        is("http://localhost/api/links/" + link.getId())))
                .andExpect(jsonPath(path + "._links.userLinkStatuses.href",
                        is("http://localhost/api/links/" + link.getId()
                                + "/linkStatuses/ARCHIVED")))
                .andExpect(jsonPath(path + "._links.shortLink.href",
                        is(shortLinkScheme + "://" + shortLinkDomain + "/" + link.getPath())));
    }

    @TestConfiguration
    @EnableHypermediaSupport(type = HAL)
    @EnableSpringDataWebSupport
    public static class TestConfig {

        @Bean
        LinkResourceAssembler linkResourceAssembler(Environment environment) {
            return new LinkResourceAssembler(environment);
        }

    }

}
