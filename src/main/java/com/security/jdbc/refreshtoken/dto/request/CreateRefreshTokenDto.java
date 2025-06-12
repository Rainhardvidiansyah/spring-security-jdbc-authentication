package com.security.jdbc.refreshtoken.dto.request;

import java.time.LocalDateTime;

public class CreateRefreshTokenDto {

    private Long userId;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean revoked;

    public CreateRefreshTokenDto(){}

    public CreateRefreshTokenDto(Long userId,
                                 String token, LocalDateTime createdAt,
                                 LocalDateTime expiresAt, boolean revoked) {

        this.userId = userId;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }


    @Override
    public String toString() {
        return "CreateRefreshTokenDto{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", revoked=" + revoked +
                '}';
    }
}
