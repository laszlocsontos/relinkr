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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.link.service.LinkService;
import com.springuni.hermes.link.model.StandaloneLink;
import com.springuni.hermes.link.web.LinkControllerTest.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(secure = false, controllers = LinkController.class)
@Import(TestConfig.class)
public class LinkControllerTest {

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
                .perform(get("/links/{linkId}", standaloneLink.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andDo(print());

        assertStandaloneLink(resultActions);
    }

    @Test
    public void addLink() throws Exception {
        LinkResource linkResource = new LinkResource(
                standaloneLink.getBaseUrl().toString(),
                standaloneLink.getUtmParameters()
        );

        given(linkService.addLink(
                linkResource.getBaseUrl(), linkResource.getUtmParameters(), 1L)
        ).willReturn(standaloneLink);

        ResultActions resultActions = mockMvc.perform(post("/links").contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isOk())
                .andDo(print());

        assertStandaloneLink(resultActions);
    }

    @Test
    public void updateLink() throws Exception {
        LinkResource linkResource = new LinkResource(
                standaloneLink.getBaseUrl().toString(),
                standaloneLink.getUtmParameters()
        );

        given(linkService.updateLink(
                standaloneLink.getId(), linkResource.getBaseUrl(), linkResource.getUtmParameters())
        ).willReturn(standaloneLink);

        ResultActions resultActions = mockMvc.perform(
                put("/links/{linkId}", standaloneLink.getId()).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkResource)))
                .andExpect(status().isOk())
                .andDo(print());

        assertStandaloneLink(resultActions);
    }

    @Test
    public void listLinks() throws Exception {
        given(linkService.listLinks(USER_ID, PAGEABLE))
                .willReturn(new PageImpl<>(asList(standaloneLink), PAGEABLE, 1));

        ResultActions resultActions = mockMvc.perform(get("/links")
                .param("page", String.valueOf(PAGEABLE.getPageNumber()))
                .param("size", String.valueOf(PAGEABLE.getPageSize())))
                .andExpect(status().isOk())
                .andDo(print());

        assertStandaloneLink("_embedded.links[0]", resultActions);
    }

    @Test
    public void updateLinkStatus_withActive() throws Exception {
        mockMvc.perform(put("/links/{linkId}/linkStatuses/{linkStatus}", standaloneLink.getId(),
                ACTIVE.name()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().activateLink(standaloneLink.getId());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void updateLinkStatus_withArchived() throws Exception {
        mockMvc.perform(put("/links/{linkId}/linkStatuses/{linkStatus}", standaloneLink.getId(),
                ARCHIVED.name()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().archiveLink(standaloneLink.getId());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void addTag() throws Exception {
        mockMvc.perform(post("/links/{linkId}/tags/{tagName}", standaloneLink.getId(),
                TAG_A.getTagName()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().addTag(standaloneLink.getId(), TAG_A.getTagName());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void removeTag() throws Exception {
        mockMvc.perform(delete("/links/{linkId}/tags/{tagName}", standaloneLink.getId(),
                TAG_A.getTagName()))
                .andExpect(status().isOk())
                .andDo(print());

        then(linkService).should().removeTag(standaloneLink.getId(), TAG_A.getTagName());
        then(linkService).shouldHaveNoMoreInteractions();
    }

    private void assertStandaloneLink(ResultActions resultActions) throws Exception {
        assertStandaloneLink("$", resultActions);
    }

    private void assertStandaloneLink(String path, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(jsonPath(path + ".baseUrl", is(standaloneLink.getBaseUrl().toString())))
                .andExpect(
                        jsonPath(path + ".targetUrl", is(standaloneLink.getTargetUrl().toString())))
                .andExpect(jsonPath(path + ".tags", hasSize(standaloneLink.getTags().size())))
                .andExpect(jsonPath(path + ".linkStatus", is(ACTIVE.name())))
                .andExpect(jsonPath(path + ".path", is(standaloneLink.getPath())))
                .andExpect(jsonPath(path + ".utmParameters.utmSource",
                        is(standaloneLink.getUtmParameters().getUtmSource())))
                .andExpect(jsonPath(path + ".utmParameters.utmMedium",
                        is(standaloneLink.getUtmParameters().getUtmMedium())))
                .andExpect(jsonPath(path + ".utmParameters.utmCampaign",
                        is(standaloneLink.getUtmParameters().getUtmCampaign())))
                .andExpect(jsonPath(path + ".utmParameters.utmTerm",
                        is(standaloneLink.getUtmParameters().getUtmTerm().orElse(null))))
                .andExpect(jsonPath(path + ".utmParameters.utmContent",
                        is(standaloneLink.getUtmParameters().getUtmContent().orElse(null))))
                .andExpect(jsonPath(path + "._links.self.href",
                        is("http://localhost/links/" + standaloneLink.getId())))
                .andExpect(jsonPath(path + "._links.userLinkStatuses.href",
                        is("http://localhost/links/" + standaloneLink.getId()
                                + "/linkStatuses/ARCHIVED")))
                .andExpect(jsonPath(path + "._links.shortLink.href",
                        is("http://localhost/" + standaloneLink.getPath())))
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
