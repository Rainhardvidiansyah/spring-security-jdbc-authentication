package com.security.jdbc.service;


import com.security.jdbc.authentication.service.UserService;
import com.security.jdbc.authentication.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepositoryImpl userRepositoryImpl;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void addUser() {
        String email = "kucing.hoki@email.com";
        String password = "password";


        Mockito.when(passwordEncoder.encode(password))
                        .thenReturn("encodedPassword");

        var result = userService.addUser(email, password);

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(password);
        Mockito.verify(userRepositoryImpl, Mockito.times(1)).addUser(email, "encodedPassword"); //must be encoded. See the service which repository.add saves email and encoded_password

        Assertions.assertEquals("User registered successfully!", result);
    }
}
