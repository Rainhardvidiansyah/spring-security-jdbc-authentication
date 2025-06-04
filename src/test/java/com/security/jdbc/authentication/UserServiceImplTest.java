package com.security.jdbc.authentication;


import com.security.jdbc.authentication.repository.UserRepository;
import com.security.jdbc.authentication.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;


    @Test
    void addUser() {
        String email = "kucing.hoki@email.com";
        String password = "password";

        UserRepository repository = Mockito.mock(UserRepository.class);

        Mockito.when(passwordEncoder.encode(password))
                        .thenReturn("encodedPassword");

        var result = userServiceImpl.addUser(email, password);

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(password);
        Mockito.verify(userRepository, Mockito.times(1)).addUser(email, "encodedPassword"); //must be encoded. See the service which repository.add saves email and encoded_password

        Assertions.assertEquals("User registered successfully!", result);
    }
}
