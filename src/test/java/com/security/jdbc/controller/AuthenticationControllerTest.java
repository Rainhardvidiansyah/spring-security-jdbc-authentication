package com.security.jdbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.jdbc.authentication.dto.request.LoginRequestDto;
import com.security.jdbc.authentication.dto.request.RegistrationRequestDto;
import com.security.jdbc.authentication.security.jwt.JwtService;
import com.security.jdbc.authentication.service.UserService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import javax.crypto.SecretKey;
import java.lang.reflect.Method;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest(properties = "access.token.secret=RxBnso0BBBeemEMOcOFrHTD67Sqgzo4M")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Test
    void encodePassword_shouldPrintDifferentResult(){
        String rawPassword = "password";

        String encodedPassword = passwordEncoder.encode(rawPassword);

        Assertions.assertNotEquals(rawPassword, encodedPassword);
    }

    @Test
    void differentPassword_shouldReturnFalse(){
        String password = "password";

        String encode = passwordEncoder.encode(password);

        Assertions.assertTrue(passwordEncoder.matches(password, encode));
    }

    @Test
    void registerUser_shouldReturnSuccessMessage() throws Exception {
        // Arrange
        String email = "rainhard@email";
        String password = "password";

        RegistrationRequestDto registrationDto = new RegistrationRequestDto();
        registrationDto.setEmail(email);
        registrationDto.setPassword(password);

        Mockito.when(userService.addUser(registrationDto.getEmail(), registrationDto.getPassword())).thenReturn("User registered successfully!");

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
        String email = "rainhard@email.com";
        String password = "password";

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail(email);
        loginRequestDto.setPassword(password);

        String body = objectMapper.writeValueAsString(loginRequestDto);

        Authentication auth = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        Mockito.when(authenticationManager.authenticate(any())).thenReturn(auth);
        String generatedToken = this.jwtService.generateTokenJwt(auth); //Perform the logic. So use the autowired annotation

        Assertions.assertNotNull(generatedToken);

        mockMvc.perform(post("/api/v1/users/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
//                .andExpect(content().string("User login successfully!"));

    }


    @Test
    void extractSubject() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("rainhard.vidi@email.com", "password");

        String generatedToken = jwtService.generateTokenJwt(authentication);

        Assertions.assertNotNull(generatedToken);
        System.out.println("GENERATED TOKEN: " + generatedToken);

        String extractedSubject = jwtService.extractSubject(generatedToken);
        Assertions.assertNotNull(extractedSubject);
        Assertions.assertEquals("rainhard.vidi@email.com", extractedSubject);

        boolean isTokenExpired = jwtService.isTokenExpired(generatedToken);
        Assertions.assertFalse(isTokenExpired);

        Claims claims = getPrivateClaims(generatedToken);
        Assertions.assertNotNull(claims);
        System.out.println("Claims: " + claims);
        //result of claims: Claims: {sub=rainhard.vidi@email.com, role=[],
        // email=rainhard.vidi@email.com, iat=1746701323, iss=ERP-MODULE-Rainhard, exp=1746704923}
        Assertions.assertEquals("rainhard.vidi@email.com", claims.get("email"));

        String extractedEmail = extractClaim(generatedToken, cl -> cl.get("email", String.class));
        Assertions.assertEquals("rainhard.vidi@email.com", extractedEmail);


    }


   //TODO: TEST EXTRACT ALL CLAIMS AND EXTRACT CLAIM
    private Claims getPrivateClaims(String token){
        try {
            Method method = JwtService.class.getDeclaredMethod("extractAllClaims", String.class);
            method.setAccessible(true);
            return (Claims) method.invoke(jwtService, token);
        }catch (Exception e){
            throw new RuntimeException("Cannot extract Claims", e);
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver){
        try {
            Method method = JwtService.class.getDeclaredMethod("extractClaim", String.class, Function.class);
            method.setAccessible(true);
            return (T) method.invoke(jwtService, token, resolver);
        }catch (Exception e){
            throw new RuntimeException("Extract claim cannot be used", e);
        }
    }

    @Test
    void getSigningKeyShouldReturnValidSecretKey() throws Exception {
        Method method = JwtService.class.getDeclaredMethod("getSigningKey");
        method.setAccessible(true);

        SecretKey key = (SecretKey) method.invoke(jwtService);

        Assertions.assertNotNull(key);
        Assertions.assertEquals("HmacSHA256", key.getAlgorithm());
    }


}

