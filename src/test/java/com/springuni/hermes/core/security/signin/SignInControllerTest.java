package com.springuni.hermes.core.security.signin;

import static com.springuni.hermes.core.security.signin.SignInController.LOGIN_VIEW;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.springuni.hermes.link.web.LinkResourceControllerTest.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(controllers = SignInController.class, secure = false)
public class SignInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginPage() throws Exception {
        mockMvc.perform(get("/login/oauth2"))
                .andExpect(status().isOk())
                .andExpect(view().name(LOGIN_VIEW))
                .andDo(print());
    }

    @TestConfiguration
    static class TestConfig {

    }

}
