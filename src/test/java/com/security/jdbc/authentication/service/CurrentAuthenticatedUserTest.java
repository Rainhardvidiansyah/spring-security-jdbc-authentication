package com.security.jdbc.authentication.service;

import com.security.jdbc.authentication.security.UserDetailsImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrentAuthenticatedUserTest {

    private final CurrentAuthenticatedUser authenticatedUser = new CurrentAuthenticatedUser();

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    @Test
    void getUserId() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(42L, "user@example.com", "password", List.of(), true);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Act
        Long userId = authenticatedUser.getUserId();

        // Assert
        assertEquals(42L, userId);
    }


    @Test
    void shouldThrowException_whenNotAuthenticated() {
        // Arrange
        SecurityContextHolder.clearContext();

        // Act & Assert
        assertThrows(IllegalStateException.class, authenticatedUser::getUserId);
    }


    @Test
    void getEmail() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(42L, "user@example.com", "password", List.of(), true);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Act
        String email = authenticatedUser.getEmail();

        // Assert
        assertEquals("user@example.com", email);
    }


    @Test
    void shouldThrowException_whenEmailNotAuthenticated() {
        // Arrange
        SecurityContextHolder.clearContext();

        // Act & Assert
        assertThrows(IllegalStateException.class, authenticatedUser::getEmail);

    }
}