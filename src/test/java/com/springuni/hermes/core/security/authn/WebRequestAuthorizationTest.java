package com.springuni.hermes.core.security.authn;

import static com.springuni.hermes.Mocks.ROOT_PATH;
import static com.springuni.hermes.Mocks.TEST_API_PATH;
import static com.springuni.hermes.Mocks.TEST_DASHBOARD_PATH;
import static com.springuni.hermes.Mocks.TEST_SHORT_LINK_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.springuni.hermes.core.security.authn.WebRequestAuthorizationTest.TestConfig;
import com.springuni.hermes.user.service.UserProfileFactory;
import com.springuni.hermes.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = WebRequestAuthorizationTestController.class)
public class WebRequestAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileFactory userProfileFactory;

    @MockBean
    private UserService userService;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void givenRootAccessed_whenUnauthenticated_thenOk() throws Exception {
        mockMvc.perform(get(ROOT_PATH))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void givenShortLinkAccessed_whenUnauthenticated_thenOk() throws Exception {
        mockMvc.perform(get(TEST_SHORT_LINK_PATH))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void givenApiAccessed_whenUnauthenticated_thenUnauthorized() throws Exception {
        mockMvc.perform(get(TEST_API_PATH))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    public void givenDashboardAccessed_whenUnauthenticated_thenRedirected() throws Exception {
        mockMvc.perform(get("/pages/dashboard"))
                .andExpect(status().isFound())
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void givenRootAccessed_whenAuthenticated_thenOk() throws Exception {
        mockMvc.perform(get(ROOT_PATH))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void givenShortLinkAccessed_whenAuthenticated_thenOk() throws Exception {
        mockMvc.perform(get(TEST_SHORT_LINK_PATH))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void givenApiAccessed_whenAuthenticated_thenOk() throws Exception {
        mockMvc.perform(get(TEST_API_PATH))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void givenDashboardAccessed_whenAuthenticated_thenOk() throws Exception {
        mockMvc.perform(get(TEST_DASHBOARD_PATH))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @TestConfiguration
    @Import(WebSecurityConfig.class)
    public static class TestConfig {

    }

}
