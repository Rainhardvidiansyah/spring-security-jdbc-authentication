package com.security.jdbc.email.controller;

import com.security.jdbc.email.dto.EmailPayloads;
import com.security.jdbc.email.service.EmailService;
import com.security.jdbc.authentication.security.UserDetailsServiceImpl;
import com.security.jdbc.authentication.security.jwt.JwtAuthEntry;
import com.security.jdbc.authentication.security.jwt.JwtAuthFilter;
import com.security.jdbc.authentication.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmailController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmailControllerTest {


    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtAuthEntry jwtAuthEntry;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;


    @Test
    void testSendEmail() throws Exception {
        EmailPayloads payload = new EmailPayloads();
        payload.setRecipient("test@example.com");
        payload.setSubject("Hello");
        payload.setMsgBody("This is a message");


        Mockito.when(emailService.SendEmailText(Mockito.any(EmailPayloads.class)))
                .thenReturn("Successfully send email to: ");

        mockMvc.perform(post("/api/v1/email/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "recipient": "test@example.com",
                                        "subject": "Hello",
                                        "msgBody": "This is a message"
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent to: test@example.com"));

        Mockito.verify(emailService, Mockito.times(1)).SendEmailText(any(EmailPayloads.class));
    }

}