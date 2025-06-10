package com.security.jdbc.authentication.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class RefreshTokenBuilderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenBuilderService.class);

    @Value("${refresh.token.secret}")
    private String refreshTokenSecret;

    private static final long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000;



    //TODO: GENERATE REFRESH TOKEN
    public String generateRefreshTokenJwt(Long userId){

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .issuer("ERP-MODULE-Rainhard")
                .expiration(expireDate)
                .signWith(getSigningKeyRefreshToken())
                .compact();
    }



    //TODO: Extract All Claims
    private Claims extractAllClaims(String token){
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSigningKeyRefreshToken())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (JwtException e){
            LOGGER.error("Error is happening here");
            throw new IllegalStateException("Token is not valid or damaged.", e);
        }
    }


    //TODO: EXTRACT CLAIM
    private  <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = this.extractAllClaims(token);
        if (claims == null) {
            throw new IllegalStateException("Invalid JWT: unable to extract claims.");
        }
        return resolver.apply(claims);
    }


    //TODO: CHECK IF TOKEN IS EXPIRED
    public boolean isTokenExpired(String token){
        return this.extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }


    public String extractSubject(String token){
        String subject = this.extractClaim(token, Claims::getSubject);
        LOGGER.info("Subject: {}", subject);
        return subject;
    }

    //TODO: EXTRACT USER ID FROM SUBJECT
    public Long extractUserIdFromSubject(String token){

        try{
            String subject = String.valueOf(this.extractSubject(token));
            return Long.parseLong(subject);
        }catch (NumberFormatException e){
            throw new RuntimeException(e);
        }
    }


    //TODO: IS TOKEN VALID METHOD
    public boolean isTokenValid(String token, Long id){
        try{
            final String subject = this.extractSubject(token);
            return (subject.equals(String.valueOf(id))) && !isTokenExpired(token);
        }catch(JwtException e){
            throw new RuntimeException(e);
        }
    }

    //TODO: GENERATE REFRESH SECRET KEY
    private SecretKey getSigningKeyRefreshToken() {
        byte[] keyBytes = refreshTokenSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //TODO: VERIFY REFRESH TOKEN. THEN GENERATE NEW TOKEN
    public Optional<String> verifyRefreshToken(HttpServletRequest request){
        if(request.getCookies() == null) return Optional.empty();

        for(Cookie cookie: request.getCookies()){
            if("refresh_token".equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }








}
