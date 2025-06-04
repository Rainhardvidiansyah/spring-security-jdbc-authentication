package com.security.jdbc.authentication.service;


import com.security.jdbc.authentication.repository.UserRepositoryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepositoryImpl userRepositoryImpl;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryImpl userRepositoryImpl, PasswordEncoder passwordEncoder) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.passwordEncoder = passwordEncoder;
    }

    public String addUser(String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        this.userRepositoryImpl.addUser(email, encodedPassword);
        return "User registered successfully!";
    }
}
