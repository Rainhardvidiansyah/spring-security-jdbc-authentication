package com.security.jdbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.jdbc.authentication.controller.AuthenticationController;
import com.security.jdbc.authentication.dto.request.LoginRequestDto;
import com.security.jdbc.authentication.dto.request.RegistrationRequestDto;
import com.security.jdbc.authentication.security.UserDetailsImpl;
import com.security.jdbc.authentication.security.UserDetailsServiceImpl;
import com.security.jdbc.authentication.security.jwt.JwtAuthEntry;
import com.security.jdbc.authentication.security.jwt.JwtAuthFilter;
import com.security.jdbc.authentication.security.jwt.JwtService;
import com.security.jdbc.authentication.security.jwt.RefreshTokenBuilderService;
import com.security.jdbc.authentication.service.UserService;
import com.security.jdbc.refreshtoken.service.RefreshTokenService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private RefreshTokenBuilderService refreshTokenBuilderService;


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
    void testAuthenticateUser_success() throws Exception {
        // Given
        String email = "test@example.com";
        String password = "password123";

        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);
        Mockito.when(userDetails.getId()).thenReturn(1L);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);
        Mockito.when(jwtService.generateTokenJwt(userDetails)).thenReturn("mocked-jwt-token");
        Mockito.when(refreshTokenBuilderService.generateRefreshTokenJwt(1L)).thenReturn("mocked-refresh-token");

        // When & Then
        mockMvc.perform(post("/api/v1/users/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("User login successfully with jwt token")));
    }


    @Test
    void getNewAccessToken_success() throws Exception {
        String mockRefreshToken = "valid-refresh-token";
        Long userId = 1L;

        UserDetailsImpl userDetails = new UserDetailsImpl(userId, "user@example.com", "hashedPassword",
                List.of(new SimpleGrantedAuthority("ROLE_USER")), true);

        Mockito.when(refreshTokenBuilderService.verifyRefreshToken(any())).thenReturn(Optional.of(mockRefreshToken));
        Mockito.when(refreshTokenBuilderService.extractUserIdFromSubject(mockRefreshToken)).thenReturn(userId);
        Mockito.when(userDetailsService.loadUserByUserId(userId)).thenReturn(userDetails);
        Mockito.when(refreshTokenBuilderService.isTokenValid(mockRefreshToken, userId)).thenReturn(true);
        Mockito.when(jwtService.generateTokenJwt(userDetails)).thenReturn("new-access-token");

        mockMvc.perform(get("/api/v1/users/auth/generate-new-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['New Access token']").value("new-access-token"));
    }



    @Test
    void getNewAccessToken_tokenInvalid() throws Exception {
        String mockRefreshToken = "invalid-token";
        Long userId = 1L;

        UserDetailsImpl userDetails = new UserDetailsImpl(userId, "user@example.com", "hashedPassword",
                List.of(), true);

        Mockito.when(refreshTokenBuilderService.verifyRefreshToken(any())).thenReturn(Optional.of(mockRefreshToken));
        Mockito.when(refreshTokenBuilderService.extractUserIdFromSubject(mockRefreshToken)).thenReturn(userId);
        Mockito.when(userDetailsService.loadUserByUserId(userId)).thenReturn(userDetails);
        Mockito.when(refreshTokenBuilderService.isTokenValid(mockRefreshToken, userId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/auth/generate-new-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token Is Not Valid"));
    }

    @Test
    void getNewAccessToken_tokenMissing() throws Exception {
        Mockito.when(refreshTokenBuilderService.verifyRefreshToken(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/users/auth/generate-new-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token Not Found"));
    }


}
