package com.security.jdbc.authentication.service;


import com.security.jdbc.authentication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String addUser(String email, String password) {
        LOGGER.info("Add User method is hit...");
        String encodedPassword = passwordEncoder.encode(password);
        this.userRepository.addUser(email, encodedPassword);
        return "User registered successfully!";
    }
}
