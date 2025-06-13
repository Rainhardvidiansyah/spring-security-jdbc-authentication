package com.security.jdbc.refreshtoken.repository;

import com.security.jdbc.refreshtoken.dto.request.CreateRefreshTokenDto;

public interface RefreshTokenRepository {

    void saveRefreshToken(CreateRefreshTokenDto refreshTokenDto);

    boolean setTokenToRevoked(Long userId);
}
