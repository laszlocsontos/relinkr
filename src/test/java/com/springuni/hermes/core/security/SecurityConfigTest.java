package com.springuni.hermes.core.security;

import static com.springuni.hermes.Mocks.ENCRYPTED_PASSWORD;
import static com.springuni.hermes.Mocks.JWT_SECRET_KEY;
import static com.springuni.hermes.Mocks.JWT_TOKEN_INVALID;
import static com.springuni.hermes.Mocks.JWT_TOKEN_VALID;
import static com.springuni.hermes.Mocks.ROOT_PATH;
import static com.springuni.hermes.Mocks.TEST_API_PATH;
import static com.springuni.hermes.Mocks.TEST_SHORT_LINK_PATH;
import static com.springuni.hermes.Mocks.createSignInRequest;
import static com.springuni.hermes.core.security.jwt.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static com.springuni.hermes.core.security.jwt.JwtAuthenticationFilter.TOKEN_PREFIX;
import static com.springuni.hermes.core.security.signin.SignInFilter.SIGNIN_PROCESSES_URL;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.security.SecurityConfigTest.TestConfig;
import com.springuni.hermes.core.security.signin.SignInRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource(properties = "jwt.secretKey=" + JWT_SECRET_KEY)
@WebMvcTest(controllers = SecuredTestController.class, secure = false)
@Ignore
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsService userDetailsService;

    private SignInRequest signInRequest;
    private UserDetails userDetails;

    @Before
    public void setUp() throws Exception {
        signInRequest = createSignInRequest();

        userDetails = mock(UserDetails.class);
        given(userDetails.getUsername()).willReturn(signInRequest.getEmailAddress());
        given(userDetails.getPassword()).willReturn(ENCRYPTED_PASSWORD);
        given(userDetails.isAccountNonExpired()).willReturn(true);
        given(userDetails.isCredentialsNonExpired()).willReturn(true);
    }

    @Test
    public void signIn_withDisabledUser() throws Exception {
        given(userDetails.isEnabled()).willReturn(false);
        given(userDetails.isAccountNonLocked()).willReturn(true);
        given(userDetailsService.loadUserByUsername(signInRequest.getEmailAddress()))
                .willReturn(userDetails);

        mockMvc.perform(
                post(SIGNIN_PROCESSES_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(signInRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.detailMessage", is("User is disabled")))
                .andDo(print());
    }

    @Test
    public void signIn_withLockedUser() throws Exception {
        given(userDetails.isEnabled()).willReturn(true);
        given(userDetails.isAccountNonLocked()).willReturn(false);
        given(userDetailsService.loadUserByUsername(signInRequest.getEmailAddress()))
                .willReturn(userDetails);

        mockMvc.perform(
                post(SIGNIN_PROCESSES_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(signInRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.detailMessage", is("User account is locked")))
                .andDo(print());
    }

    @Test
    public void signIn_withValidUsernameAndPassword() throws Exception {
        given(userDetails.isEnabled()).willReturn(true);
        given(userDetails.isAccountNonLocked()).willReturn(true);
        given(userDetailsService.loadUserByUsername(signInRequest.getEmailAddress()))
                .willReturn(userDetails);

        mockMvc.perform(
                post(SIGNIN_PROCESSES_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Set-Authorization-Bearer"))
                .andDo(print());
    }

    @Test
    public void accessRoot_unauthenticated() throws Exception {
        mockMvc.perform(get(ROOT_PATH))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void accessShortLink_unauthenticated() throws Exception {
        mockMvc.perform(get(TEST_SHORT_LINK_PATH))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void accessApi_unauthenticated() throws Exception {
        mockMvc.perform(get(TEST_API_PATH))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detailMessage",
                        is("Full authentication is required to access this resource")))
                .andDo(print());
    }

    @Test
    public void authenticate_withInValidJwtToken() throws Exception {
        mockMvc.perform(get(TEST_API_PATH)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + " " + JWT_TOKEN_INVALID))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detailMessage",
                        is("Full authentication is required to access this resource")))
                .andDo(print());
    }

    @Test
    public void authenticate_withValidJwtToken() throws Exception {
        mockMvc.perform(get(TEST_API_PATH)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + " " + JWT_TOKEN_VALID))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @TestConfiguration
    @Import(SecurityConfig.class)
    public static class TestConfig {

    }

}
