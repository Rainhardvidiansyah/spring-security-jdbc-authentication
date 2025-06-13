package com.security.jdbc.refreshtoken.repository;

import com.security.jdbc.refreshtoken.dto.request.CreateRefreshTokenDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class RefreshTokenRepositoryImplTest {


    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RefreshTokenRepositoryImpl refreshTokenRepository;


    @Test
    void shouldSaveRefreshToken() {
        // Arrange
        CreateRefreshTokenDto dto = new CreateRefreshTokenDto();
        dto.setUserId(1L);
        dto.setToken("mock-token");
        dto.setExpiresAt(LocalDateTime.now().plusDays(7));
        dto.setRevoked(false);

        // Act
        refreshTokenRepository.saveRefreshToken(dto);

        // Assert
        Mockito.verify(jdbcTemplate).update(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(dto.getUserId()),
                ArgumentMatchers.eq(dto.getToken()),
                ArgumentMatchers.eq(Timestamp.valueOf(dto.getExpiresAt())),
                ArgumentMatchers.eq(dto.isRevoked())
        );
    }

    @Test
    void shouldThrowRuntimeExceptionOnDataAccessException() {
        // Arrange
        CreateRefreshTokenDto dto = new CreateRefreshTokenDto();
        dto.setUserId(1L);
        dto.setToken("mock-token");
        dto.setExpiresAt(LocalDateTime.now().plusDays(7));
        dto.setRevoked(false);

        Mockito.doThrow(new DataAccessException("DB error"){}).when(jdbcTemplate)
                .update(ArgumentMatchers.anyString(), ArgumentMatchers.any(),
                        ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> refreshTokenRepository.saveRefreshToken(dto));
    }


    @Test
    void shouldSetTokenToRevoked() {
        // given
        Long userId = 1L;

        Mockito.when(jdbcTemplate.update(ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(userId))).thenReturn(1);

        // when
        boolean result = refreshTokenRepository.setTokenToRevoked(userId);

        // then
        assertTrue(result);

        Mockito.verify(jdbcTemplate).update(ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(userId));
    }


    @Test
    void setTokenToRevoked_shouldReturnFalse_whenNoRowAffected() {
        // given
        Long userId = 2L;
        Mockito.when(jdbcTemplate.update(ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(userId))).thenReturn(0); // no row updated

        // when
        boolean result = refreshTokenRepository.setTokenToRevoked(userId);

        // then
        assertFalse(result);
        Mockito.verify(jdbcTemplate).update(ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(userId));
    }




}