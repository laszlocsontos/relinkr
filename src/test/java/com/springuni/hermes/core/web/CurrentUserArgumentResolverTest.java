package com.springuni.hermes.core.web;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.springuni.hermes.core.security.authn.annotation.CurrentUser;
import com.springuni.hermes.core.web.CurrentUserArgumentResolverTest.TestConfig;
import com.springuni.hermes.core.web.CurrentUserArgumentResolverTest.TestController;
import com.springuni.hermes.user.model.UserId;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = TestController.class, secure = false)
public class CurrentUserArgumentResolverTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "1") // USER_ID
    public void givenAuthenticatedUser_whenResolveUserId_thenOk() throws Exception {
        mockMvc.perform(
                get("/").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "a") // USER_ID
    public void givenNonNumericUserId_whenResolveUserId_thenError() throws Exception {
        mockMvc.perform(
                get("/").contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Test
    public void givenNoAuthenticatedUser_whenResolveUserId_thenNoContent()
            throws Exception {

        mockMvc.perform(
                get("/").contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @TestConfiguration
    @Import(WebMvcConfig.class)
    public static class TestConfig {

    }

    @RestController
    public static class TestController {

        @GetMapping("/")
        public HttpEntity get(@CurrentUser UserId userId) {
            return Optional.ofNullable(userId)
                    .map(UserId::getId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.noContent().build());
        }

    }

}
