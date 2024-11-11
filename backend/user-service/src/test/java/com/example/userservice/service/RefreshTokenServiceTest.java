package com.example.userservice.service;

import com.example.userservice.entity.RefreshToken;
import com.example.userservice.exception.RefreshTokenException;
import com.example.userservice.repo.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        refreshTokenService.setRefreshTokenExpiration(Duration.ofMillis(3600000));
    }

    @Test
    void findByRefreshToken_ShouldReturnRefreshToken() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(1L)
                .token(token)
                .expiryDate(Instant.now().plus(refreshTokenService.getRefreshTokenExpiration()))
                .build();

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> foundToken = refreshTokenService.findByRefreshToken(token);

        assertTrue(foundToken.isPresent());
        assertEquals(token, foundToken.get().getToken());
    }

    @Test
    void createRefreshToken_ShouldReturnNewRefreshToken() {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(1L)
                .expiryDate(Instant.now().plus(refreshTokenService.getRefreshTokenExpiration()))
                .token(UUID.randomUUID().toString())
                .build();

        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

        RefreshToken createdToken = refreshTokenService.createRefreshToken(1L);

        assertNotNull(createdToken);
        assertEquals(1L, createdToken.getUserId());
        assertTrue(createdToken.getExpiryDate().isAfter(Instant.now()));
        assertNotNull(createdToken.getToken());
    }

    @Test
    void checkRefreshToken_ShouldReturnSameTokenIfNotExpired() {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(1L)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(refreshTokenService.getRefreshTokenExpiration()))
                .build();

        RefreshToken result = refreshTokenService.checkRefreshToken(refreshToken);

        assertNotNull(result);
        assertEquals(refreshToken, result);
    }

    @Test
    void checkRefreshToken_ShouldThrowExceptionIfExpired() {
        RefreshToken expiredToken = RefreshToken.builder()
                .userId(1L)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().minusMillis(1000)) // expired 1 second ago
                .build();

        doNothing().when(refreshTokenRepository).delete(expiredToken);

        RefreshTokenException exception = assertThrows(
                RefreshTokenException.class,
                () -> refreshTokenService.checkRefreshToken(expiredToken)
        );

        assertEquals("Refresh token was expired. Repeat signin action", exception.getMessage());
        verify(refreshTokenRepository, times(1)).delete(expiredToken);
    }

    @Test
    void deleteByUserId_ShouldDeleteRefreshToken() {
        Long userId = 1L;
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(refreshTokenService.getRefreshTokenExpiration()))
                .build();

        when(refreshTokenRepository.findByUserId(userId)).thenReturn(Optional.of(refreshToken));
        doNothing().when(refreshTokenRepository).delete(refreshToken);

        refreshTokenService.deleteByUserId(userId);

        verify(refreshTokenRepository, times(1)).delete(refreshToken);
    }

    @Test
    void deleteByUserId_ShouldThrowExceptionIfTokenNotFound() {
        Long userId = 1L;
        when(refreshTokenRepository.findByUserId(userId)).thenReturn(Optional.empty());

        RefreshTokenException exception = assertThrows(
                RefreshTokenException.class,
                () -> refreshTokenService.deleteByUserId(userId)
        );

        assertEquals("Refresh token not found", exception.getMessage());
    }
}
