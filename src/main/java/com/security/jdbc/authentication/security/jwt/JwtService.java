package com.security.jdbc.authentication.security.jwt;

import com.security.jdbc.authentication.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    @Value("${access.token.secret}")
    private String jwtSecret;

    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    private static final long accessTokenExpiration = 15 * 60 * 1000;





    //TODO: GENERATE TOKEN JWT
    public String generateTokenJwt(UserDetailsImpl userDetails){

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getUsername());
        claims.put("roles", roles);

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(userDetails.getId()))
                .claims(claims)
                .issuedAt(new Date())
                .issuer("ERP-MODULE-Rainhard")
                .expiration(expireDate)
                .signWith(getSigningKey())
                .compact();
    }


    //TODO: GET EMAIL FROM JWT TOKEN AND JUST TRY TO GET CLAIMS
    public String getEmailFromJWT(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }


    //TODO: CHECK IF TOKEN VALID
    public boolean isTokenValid(String token, UserDetailsImpl userDetails){
        try{
            final String subject = this.extractSubject(token);
            return (subject.equals(String.valueOf(userDetails.getId()))) && !isTokenExpired(token);
        }
       catch(JwtException e){
            e.printStackTrace();
            return false;
       }
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


    //TODO: Extract All Claims
    private Claims extractAllClaims(String token){
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSigningKey())
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
        return this.extractClaim(token, Claims::getExpiration).before(new Date());
    }


    //TODO: GENERATE SECRET KEY
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //TODO: EXTRACT SUBJECT
    public String extractSubject(String token){
        String subject = this.extractClaim(token, Claims::getSubject);
        LOGGER.info("Subject: {}", subject);
        return subject;
    }





}

/*
Custom Claims
If you need to set one or more custom claims that don’t match the standard setter method claims shown above,
you can simply call the JwtBuilder claim method one or more times as needed:

    String jws = Jwts.builder()

    .claim("hello", "world") -> result: hello, world

    // ... etc ...
 */