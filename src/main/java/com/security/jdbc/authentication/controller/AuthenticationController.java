package com.security.jdbc.authentication.controller;



import com.security.jdbc.authentication.dto.request.LoginRequestDto;
import com.security.jdbc.authentication.dto.request.RegistrationRequestDto;
import com.security.jdbc.authentication.security.UserDetailsImpl;
import com.security.jdbc.authentication.security.UserDetailsServiceImpl;
import com.security.jdbc.authentication.security.jwt.JwtService;
import com.security.jdbc.authentication.security.jwt.RefreshTokenBuilderService;
import com.security.jdbc.authentication.service.UserService;
import com.security.jdbc.refreshtoken.dto.request.CreateRefreshTokenDto;
import com.security.jdbc.refreshtoken.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenBuilderService refreshTokenBuilderService;

    private final UserDetailsServiceImpl userDetailsService;

    private final RefreshTokenService refreshTokenService;



    public AuthenticationController(UserService userService,
                                    AuthenticationManager authenticationManager,
                                    JwtService jwtService,
                                    RefreshTokenService refreshTokenService,
                                    RefreshTokenBuilderService refreshTokenBuilderService,
                                    UserDetailsServiceImpl userDetailsService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenBuilderService = refreshTokenBuilderService;
        this.userDetailsService = userDetailsService;
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
    public String authenticateUser(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response){

        LOGGER.info("Login method is called");

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                                (loginRequest.getEmail(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String generatedJwt = this.jwtService.generateTokenJwt(userDetails);

        String generatedRefreshToken = this.refreshTokenBuilderService.generateRefreshTokenJwt(userDetails.getId());

        var refreshTokenDto = new CreateRefreshTokenDto();
        refreshTokenDto.setUserId(userDetails.getId());
        refreshTokenDto.setToken(generatedRefreshToken);

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime futureDate = now.plusDays(7);

        refreshTokenDto.setCreatedAt(now);

        refreshTokenDto.setExpiresAt(futureDate);

        LOGGER.info("Authenticate User => Data of refresh Token Dto: {}", refreshTokenDto);

        //this.refreshTokenService.saveRefreshToken(refreshTokenDto);

        Cookie cookie = new Cookie("refresh_token", generatedRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);  //for seven days

        response.addCookie(cookie);

        return "User login successfully with jwt token: " + generatedJwt;
    }




    @GetMapping("/generate-new-token")
    public ResponseEntity<?> getNewAccessToken(HttpServletRequest request){
        LOGGER.info("Get New Access Token is hit");

        // 1. get token from cookie
        Optional<String> tokenOpt = this.refreshTokenBuilderService.verifyRefreshToken(request);

        if(tokenOpt.isEmpty()){
            return new ResponseEntity<>("Token Not Found", HttpStatus.UNAUTHORIZED);
        }

        // 2. Extract token
        Long id = this.refreshTokenBuilderService.extractUserIdFromSubject(tokenOpt.get());


        // 3. Get user id from user details as the token saved id
        UserDetailsImpl userDetails = (UserDetailsImpl) this.userDetailsService.loadUserByUserId(id);


        // 4. Validate token or compare token and user id
        boolean validateToken = this.refreshTokenBuilderService.isTokenValid(tokenOpt.get(), userDetails.getId());

        LOGGER.info("Validate token, {}", validateToken);

        if(!validateToken){
            return new ResponseEntity<>("Token Is Not Valid", HttpStatus.UNAUTHORIZED);
        }

        // 5. Generate new access token
        String generatedNewToken = this.jwtService.generateTokenJwt(userDetails);


        return new ResponseEntity<>(Map.of("New Access token", generatedNewToken), HttpStatus.OK);
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){

        Cookie cookie = new Cookie("refresh_token", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        LOGGER.info("COOKIE: {}", cookie.getValue());
        return new ResponseEntity<>("User Logout", HttpStatus.OK);
    }


    //TODO: IMPLEMENT REFRESH TOKEN TO SAVE THE REFRESH TOKEN AND REVOKE THE REFRESH TOKEN!!!
    //TODO: IT IS MANDATORY!!


}
