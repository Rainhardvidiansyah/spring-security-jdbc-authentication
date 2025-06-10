package com.security.jdbc.refreshtoken.service;

import com.security.jdbc.refreshtoken.dto.request.CreateRefreshTokenDto;
import com.security.jdbc.refreshtoken.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{


    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);


    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void saveRefreshToken(CreateRefreshTokenDto refreshTokenDto) {

        if(refreshTokenDto==null){
            throw new RuntimeException("Refresh token object is null");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusDays(7);
        refreshTokenDto.setExpiresAt(expiryDate);

        LOGGER.info("Save Refresh Token -> Data refresh token: {}", refreshTokenDto);


        this.refreshTokenRepository.saveRefreshToken(refreshTokenDto);

    }
}
