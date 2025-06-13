package com.security.jdbc.refreshtoken.service;

import com.security.jdbc.refreshtoken.dto.request.CreateRefreshTokenDto;
import com.security.jdbc.refreshtoken.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {


    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenServiceImpl;


    @Test
    void shouldThrowExceptionWhenDtoIsNull() {
        assertThrows(RuntimeException.class, () -> refreshTokenServiceImpl.saveRefreshToken(null));
    }

    @Test
    void shouldSetExpiryAndSaveToken() {
        CreateRefreshTokenDto dto = new CreateRefreshTokenDto();
        dto.setUserId(1L);
        dto.setToken("mock-token");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setRevoked(false);
        dto.setExpiresAt(LocalDateTime.now().plusDays(7));

        refreshTokenServiceImpl.saveRefreshToken(dto);

        assertNotNull(dto.getExpiresAt());
        assertTrue(dto.getExpiresAt().isAfter(dto.getCreatedAt()));

        Mockito.verify(refreshTokenRepository).saveRefreshToken(dto);
    }

    @Test
    void revokeToken() {
        Long userId = 1L;

        Mockito.when(refreshTokenRepository.setTokenToRevoked(userId))
                .thenReturn(true);

        boolean token = refreshTokenServiceImpl.revokeToken(userId);

        Assertions.assertTrue(token);

        Mockito.verify(refreshTokenRepository).setTokenToRevoked(userId);
    }

    @Test
    void failedRevoke_shouldReturnRuntimeException(){
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class,
                ()-> {refreshTokenServiceImpl.revokeToken(0L);
        });

        Assertions.assertEquals("User id cannot be 0", runtimeException.getMessage());

        Mockito.verifyNoInteractions(refreshTokenRepository);

    }
}