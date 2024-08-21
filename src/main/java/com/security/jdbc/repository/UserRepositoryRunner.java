package com.security.jdbc.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserRepositoryRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("USER RUNNER: " + userRepository.findOneUser("rainhard@email.com"));
        System.out.println("AUTHORITY RUNNER: " + userRepository.getUserAuthoritiesByUserEmail("rainhard@email.com"));

    }
}
