package com.example.userservice.controller;


import com.example.userservice.dto.*;
import com.example.userservice.security.UserDetailsServiceImpl;
import com.example.userservice.security.jwt.JwtUtils;
import com.example.userservice.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateUser_ShouldReturnAuthRs() throws Exception {
        LoginRq loginRq = new LoginRq("username", "password");
        AuthRs authRs = AuthRs.builder()
                .refreshToken("refreshToken")
                .token("accessToken")
                .build();

        when(authService.authenticateUser(any(LoginRq.class))).thenReturn(authRs);

        mockMvc.perform(post(baseUrl + "/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User login successfully"))
                .andExpect(jsonPath("$.data.token").value("accessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("refreshToken"));
    }

    @Test
    void register_ShouldReturnSuccessMessage() throws Exception {
        UserRq rq = UserRq.builder()
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles(List.of())
                .build();

        doNothing().when(authService).register(any(UserRq.class));

        mockMvc.perform(post(baseUrl + "/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User registered"));
    }

    @Test
    void refreshToken_ShouldReturnRefreshTokenRs() throws Exception {
        RefreshTokenRq refreshTokenRq = new RefreshTokenRq("refreshToken");
        RefreshTokenRs refreshTokenRs = new RefreshTokenRs("newAccessToken", "newRefreshToken");

        when(authService.refreshToken(any(RefreshTokenRq.class))).thenReturn(refreshTokenRs);

        mockMvc.perform(post(baseUrl + "/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Refresh token returned"))
                .andExpect(jsonPath("$.data.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("newRefreshToken"));
    }

    @Test
    @WithMockUser(username = "testUser") // симулируем авторизованного пользователя
    void logout_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(authService).logout();

        mockMvc.perform(post(baseUrl + "/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User logout. User name is: testUser"));
    }
}

