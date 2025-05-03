package com.security.jdbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.jdbc.dto.request.LoginRequestDto;
import com.security.jdbc.dto.request.RegistrationRequestDto;
import com.security.jdbc.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void registerUser_shouldReturnSuccessMessage() throws Exception {
        // Arrange
        String email = "rainhard@email";
        String password = "password";

        RegistrationRequestDto registrationDto = new RegistrationRequestDto();
        registrationDto.setEmail(email);
        registrationDto.setPassword(password);

        Mockito.when(userService.addUser(email, password)).thenReturn("User registered successfully!");

        // Act
        mockMvc.perform(post("/api/v1/users/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully!"));


        // Verify
        Mockito.verify(userService, Mockito.times(1)).addUser(email, password);
    }


    @Test
    void login() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("rainhard@email.com", "password");

        String body = objectMapper.writeValueAsString(loginRequestDto);

        Authentication auth = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        Mockito.when(authenticationManager.authenticate(any())).thenReturn(auth);

        mockMvc.perform(post("/api/v1/users/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
                //.andExpect(content().string("User login successfully!"));

    }


}

