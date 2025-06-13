package com.security.jdbc.refreshtoken.repository;

import com.security.jdbc.refreshtoken.dto.request.CreateRefreshTokenDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository{

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public RefreshTokenRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveRefreshToken(CreateRefreshTokenDto refreshTokenDto) {
        try{
            String sql =
                    "INSERT INTO refresh_token(user_id, token, expires_at, revoked) VALUES(?, ?, ?, ?)";

            jdbcTemplate.update(sql,
                    refreshTokenDto.getUserId(),
                    refreshTokenDto.getToken(),
                    Timestamp.valueOf(refreshTokenDto.getExpiresAt()),
                    refreshTokenDto.isRevoked());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean setTokenToRevoked(Long userId) {

        try{
            String sql = "UPDATE refresh_token SET revoked = TRUE WHERE user_id = ?";

            int affectedRow = jdbcTemplate.update(sql, userId);

            return affectedRow > 0;
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }
    }


}
