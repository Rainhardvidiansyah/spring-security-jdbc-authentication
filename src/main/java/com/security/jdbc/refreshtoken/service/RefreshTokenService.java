package com.security.jdbc.refreshtoken.service;

import com.security.jdbc.refreshtoken.dto.request.CreateRefreshTokenDto;

public interface RefreshTokenService {

    void saveRefreshToken(CreateRefreshTokenDto refreshTokenDto);
}
