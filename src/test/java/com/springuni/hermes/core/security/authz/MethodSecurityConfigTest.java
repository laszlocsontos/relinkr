package com.springuni.hermes.core.security.authz;

import static com.springuni.hermes.test.Mocks.USER_ID;
import static javax.persistence.LockModeType.NONE;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.springuni.hermes.core.convert.StringToEntityClassAwareIdConverter;
import com.springuni.hermes.core.security.authz.MethodSecurityConfigTest.TestConfig;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = MethodSecurityTestController.class)
public class MethodSecurityConfigTest {

    private static final TestId TEST_ID = TestId.of(1L);
    private static final TestOwnable TEST_OWNABLE = TestOwnable.of(USER_ID);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntityManager entityManager;

    @Test
    @WithMockUser(username = "1") // USER_ID
    public void givenCurrentUserIsOwner_thenAccessGranted() throws Exception {
        given(entityManager.find(TestOwnable.class, TEST_ID, NONE)).willReturn(TEST_OWNABLE);

        mockMvc.perform(
                get("/{testId}/with-annotation", TEST_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "0", roles = "ADMIN") // USER_ID_ZERO
    public void givenCurrentUserIsAdminButNotOwner_thenAccessGranted() throws Exception {
        given(entityManager.find(TestOwnable.class, TEST_ID, NONE)).willReturn(TEST_OWNABLE);

        mockMvc.perform(
                get("/{testId}/with-annotation", TEST_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "0") // USER_ID_ZERO
    public void givenCurrentUserIsNotOwner_thenAccessDenied() throws Exception {
        given(entityManager.find(TestOwnable.class, TEST_ID, NONE)).willReturn(TEST_OWNABLE);

        mockMvc.perform(
                get("/{testId}/with-annotation", TEST_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "0") // USER_ID_ZERO
    public void givenMethodSecurityIsNotEnforced_thenAccessGranted() throws Exception {
        mockMvc.perform(
                get("/{testId}/without-annotation", TEST_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @TestConfiguration
    @Import(MethodSecurityConfig.class)
    public static class TestConfig implements WebMvcConfigurer {

        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addConverter(String.class, TestId.class,
                    new StringToEntityClassAwareIdConverter<>(TestId.class));

        }

    }

}
