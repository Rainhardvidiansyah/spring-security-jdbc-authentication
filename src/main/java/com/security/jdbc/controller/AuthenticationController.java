package com.security.jdbc.controller;


import com.security.jdbc.dto.request.LoginRequestDto;
import com.security.jdbc.dto.request.RegistrationRequestDto;
import com.security.jdbc.security.UserDetailsImpl;
import com.security.jdbc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;

    private final AuthenticationManager authenticationManager;


    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody RegistrationRequestDto registrationRequestDto){
        LOGGER.info("Registration method is called");


        // perform user registration logic here

        LOGGER.info("Registering user: {}", registrationRequestDto.getEmail());

        this.userService.addUser(registrationRequestDto.getEmail(), registrationRequestDto.getPassword());
        return "User registered successfully!";
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody LoginRequestDto loginRequest){

        LOGGER.info("Login method is called");

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                                (loginRequest.getEmail(), loginRequest.getPassword()));

        LOGGER.debug("Authentication content: {}", authentication.getName());
        if(!authentication.isAuthenticated()){
            return "user not authenticated";
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LOGGER.info("User authenticated successfully: {}", authentication.getName());

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        LOGGER.info("Authorities that user has: {}", authentication.getAuthorities());
        LOGGER.info("Authorities from User details: {}", userDetails.getAuthorities());
        LOGGER.info("is user enabled: {}", userDetails.isEnabled());


        return "User login successfully!";
    }
}
