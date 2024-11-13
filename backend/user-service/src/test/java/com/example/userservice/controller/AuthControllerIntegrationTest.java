package com.example.userservice.controller;


import com.example.userservice.dto.LoginRq;
import com.example.userservice.dto.StatusCode;
import com.example.userservice.dto.UserRq;
import com.example.userservice.entity.User;
import com.example.userservice.security.jwt.JwtUtils;
import com.example.userservice.service.RefreshTokenService;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    private String validRefreshToken;
    private String validAccessToken;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12.3")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    private static final GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:7.0.12"))
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());

    @BeforeAll
    public static void startContainer() {
        redisContainer.start();
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void setDataSourceProperties(@NotNull DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");

        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @Test
    public void testRegisterSuccessfully() throws Exception {
        UserRq rq = UserRq.builder()
                .username("test")
                .email("test@mail.com")
                .password("password")
                .roles(List.of())
                .build();

        mockMvc.perform(post(baseUrl + "/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User registered"));
    }

    @Test
    public void testAuthenticateSuccess() throws Exception {
        LoginRq rq = new LoginRq("admin", "passadmin");

        mockMvc.perform(post(baseUrl + "/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User login successfully"));
    }

    @Test
    public void testAuthenticateFail() throws Exception {
        LoginRq rq = new LoginRq("admin", "passadmi");

        mockMvc.perform(post(baseUrl + "/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rq)))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value("username or password is incorrect"))
                .andExpect(jsonPath("$.data").value("Bad credentials"));
    }

    @Test
    public void testRefreshTokenSuccess() throws Exception {

        User adminUser = userService.findByUsername("admin");
        validRefreshToken = refreshTokenService.createRefreshToken(adminUser.getId()).getToken();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("refreshToken", validRefreshToken);

        mockMvc.perform(post(baseUrl + "/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Refresh token returned"))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());
    }

    @Test
    public void testRefreshTokenFailure() throws Exception {
        User adminUser = userService.findByUsername("admin");
        validRefreshToken = refreshTokenService.createRefreshToken(adminUser.getId()).getToken();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("refreshToken", "invalid_token");

        mockMvc.perform(post(baseUrl + "/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.FORBIDDEN))
                .andExpect(jsonPath("$.message").value("Error trying to refresh by token: invalid_token : Refresh token not found"));
    }

    @Test
    public void testLogoutSuccess() throws Exception {
        User adminUser = userService.findByUsername("admin");
        validRefreshToken = refreshTokenService.createRefreshToken(adminUser.getId()).getToken();
        validAccessToken = jwtUtils.generateTokenFromUsername("admin");

        mockMvc.perform(post(baseUrl + "/auth/logout")
                        .header("Authorization", "Bearer " + validAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User logout. User name is: admin"));
    }

    @Test
    public void testLogoutFailureNoAuth() throws Exception {

        mockMvc.perform(post(baseUrl + "/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value("Access Denied"));
    }

}

