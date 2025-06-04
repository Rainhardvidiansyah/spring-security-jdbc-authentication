package com.security.jdbc.security.jwt;

import com.security.jdbc.authentication.security.jwt.JwtAuthEntry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;


@ExtendWith(MockitoExtension.class)
class JwtAuthEntryTest {


    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @InjectMocks
    private JwtAuthEntry jwtAuthEntry;

    @Test
    void commence() throws ServletException, IOException {
        jwtAuthEntry.commence(request, response, authException);

        Mockito.verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized user cannot access the protected resource");
    }



}