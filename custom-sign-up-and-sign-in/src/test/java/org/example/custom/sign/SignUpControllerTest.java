package org.example.custom.sign;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SignUpController.class)
@Import(WebSecurityConfig.class)
class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserAccountService userAccountService;

    @MockitoBean
    private UserAccountDetailsService userAccountDetailsService;

    @Test
    void shouldRenderSignupForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @Test
    void shouldRejectMissingEmailOrPassword() throws Exception {
        mockMvc.perform(post("/signup").with(csrf())
                        .param("email", "")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("signup"));

        verifyNoInteractions(userAccountService);
    }

    @Test
    void shouldRegisterWhenFormValid() throws Exception {
        UserAccount saved = new UserAccount("user@example.com", "encoded");
        when(userAccountService.register(anyString(), anyString())).thenReturn(saved);

        mockMvc.perform(post("/signup").with(csrf())
                        .param("email", "user@example.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(userAccountService).register(anyString(), anyString());
    }
}
