package com.security.jdbc.authentication.service;


import com.security.jdbc.authentication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String addUser(String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        this.userRepository.addUser(email, encodedPassword);
        return "User registered successfully!";
    }
}
