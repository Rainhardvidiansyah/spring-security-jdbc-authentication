package com.security.jdbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.jdbc.dto.request.LoginRequestDto;
import com.security.jdbc.dto.request.RegistrationRequestDto;
import com.security.jdbc.security.UserDetailsServiceImpl;
import com.security.jdbc.security.jwt.JwtAuthEntry;
import com.security.jdbc.security.jwt.JwtAuthFilter;
import com.security.jdbc.security.jwt.JwtService;
import com.security.jdbc.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

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

    /*
    Load JwtAuthFilter and JwtAuthEntry here.
    This is mandatory as WebMvcTest doesn't know
    the real flow behind the program.
    This is difficult to maintain, but we can use test class
    annotated with SpringBOotTest.
     */

    @Test
    void whenUserRegister_thenReturn_isCreated() throws Exception {

        RegistrationRequestDto registrationDto = new RegistrationRequestDto();
        registrationDto.setEmail("maul@email.com");
        registrationDto.setPassword("password");

        Mockito.when(userService.addUser(registrationDto.getEmail(), registrationDto.getPassword()))
                .thenReturn("User registered successfully!");

        String body = objectMapper.writeValueAsString(registrationDto);

        mockMvc.perform(post("/api/v1/users/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(content().string("User registered successfully!"));

        Mockito.verify(userService, Mockito.times(1))
                .addUser(registrationDto.getEmail(), registrationDto.getPassword());
    }


    @Test
    void login_shouldResponseWithOk() throws Exception {
        String email = "maul@email.com";
        String password = "password";
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail(email);
        loginRequestDto.setPassword(password);

        String body = objectMapper.writeValueAsString(loginRequestDto);

        Authentication auth = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        Mockito.when(authenticationManager.authenticate(any())).thenReturn(auth);
        Mockito.when(jwtService.generateTokenJwt(auth)).thenReturn(ArgumentMatchers.anyString());

        mockMvc.perform(post("/api/v1/users/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
                //.andExpect(content().string("User login successfully!"));

    }

}
