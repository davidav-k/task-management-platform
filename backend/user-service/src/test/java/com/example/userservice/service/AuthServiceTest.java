package com.example.userservice.service;


import com.example.userservice.dto.*;
import com.example.userservice.entity.RefreshToken;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.RoleType;
import com.example.userservice.entity.User;
import com.example.userservice.exception.RefreshTokenException;
import com.example.userservice.repo.UserRepository;
import com.example.userservice.security.AppUserDetails;
import com.example.userservice.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateUser_ShouldReturnAuthRs() {
        Role roleAdmin = Role.builder().name(RoleType.ROLE_ADMIN).build();
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles(Set.of(roleAdmin))
                .enabled(true)
                .build();
        AppUserDetails userDetails = new AppUserDetails(user);
        Authentication authentication = mock(Authentication.class);
        RefreshToken refreshToken = RefreshToken.builder()
                .id(1L)
                .token("refreshToken")
                .userId(userDetails.getId())
                .build();
        LoginRq loginRq = new LoginRq("admin", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(userDetails)).thenReturn("accessToken");
        when(refreshTokenService.createRefreshToken(userDetails.getId())).thenReturn(refreshToken);

        AuthRs authRs = authService.authenticateUser(loginRq);

        assertNotNull(authRs);
        assertEquals(user.getId(), authRs.getId());
        assertEquals("accessToken", authRs.getToken());
        assertEquals("refreshToken", authRs.getRefreshToken());
        assertEquals("admin", authRs.getUsername());
        assertEquals("admin@mail.com", authRs.getEmail());
        assertEquals(List.of("ROLE_ADMIN"), authRs.getRoles());
    }

    @Test
    void register_ShouldCallCreateUser() {
        UserRq rq = UserRq.builder()
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles(List.of("ROLE_ADMIN"))
                .enable(true)
                .build();

        when(userService.createUser(any(UserRq.class))).thenReturn(any(UserRs.class));

        authService.register(rq);

        verify(userService, times(1)).createUser(any(UserRq.class));
    }

    @Test
    void refreshToken_ShouldReturnNewTokens() {
        Role roleAdmin = Role.builder().name(RoleType.ROLE_ADMIN).build();
        RefreshTokenRq refreshTokenRq = new RefreshTokenRq("oldRefreshToken");
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles(Set.of(roleAdmin))
                .enabled(true)
                .build();
        RefreshToken oldRefreshToken = RefreshToken.builder()
                .id(1L)
                .token("oldRefreshToken")
                .userId(1L)
                .build();
        RefreshToken newRefreshToken = RefreshToken.builder()
                .id(1L)
                .token("newRefreshToken")
                .userId(1L)
                .build();

        when(refreshTokenService.findByRefreshToken(anyString())).thenReturn(Optional.of(oldRefreshToken));
        when(refreshTokenService.checkRefreshToken(oldRefreshToken)).thenReturn(oldRefreshToken);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(jwtUtils.generateTokenFromUsername(user.getUsername())).thenReturn("newAccessToken");
        when(refreshTokenService.createRefreshToken(user.getId())).thenReturn(newRefreshToken);

        RefreshTokenRs refreshTokenRs = authService.refreshToken(refreshTokenRq);

        assertNotNull(refreshTokenRs);
        assertEquals("newAccessToken", refreshTokenRs.getAccessToken());
        assertEquals("newRefreshToken", refreshTokenRs.getRefreshToken());
    }

    @Test
    void refreshToken_ShouldThrowException_WhenTokenNotFound() {
        RefreshTokenRq refreshTokenRq = new RefreshTokenRq("invalidToken");

        when(refreshTokenService.findByRefreshToken("invalidToken")).thenReturn(Optional.empty());

        assertThrows(RefreshTokenException.class, () -> authService.refreshToken(refreshTokenRq));
    }

    @Test
    void logout_ShouldDeleteRefreshToken() {
        Role roleAdmin = Role.builder().name(RoleType.ROLE_ADMIN).build();
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles(Set.of(roleAdmin))
                .enabled(true)
                .build();
        AppUserDetails userDetails = new AppUserDetails(user);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        doNothing().when(refreshTokenService).deleteByUserId(userDetails.getId());

        authService.logout();

        verify(refreshTokenService, times(1)).deleteByUserId(userDetails.getId());
    }
}
