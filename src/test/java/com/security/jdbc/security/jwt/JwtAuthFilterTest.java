package com.security.jdbc.security.jwt;


import com.security.jdbc.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    void testDoFilterInternal_withValidToken() throws Exception {
        // Arrange
        String token = "Bearer valid.jwt.token";
        Mockito.when(request.getHeader("Authorization")).thenReturn(token);
        Mockito.when(jwtService.extractSubject(anyString())).thenReturn("user@example.com");
        Mockito.when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);

        UserDetails userDetails = new User("user@example.com", "password", new ArrayList<>());
        Mockito.when(userDetailsServiceImpl.loadUserByUsername("user@example.com")).thenReturn(userDetails);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Mockito.verify(filterChain).doFilter(request, response);
    }
}