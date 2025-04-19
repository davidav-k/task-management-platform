package com.example.user_service.resource;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.UserRequest;
import com.example.user_service.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.Container;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToServer;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@ExtendWith(SpringExtension.class)
class UserResourceIntegrationTest {

    @JavaDispatcher.Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        baseUrl = "http://localhost:" + port + "/api/v1/user";
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("John");
        userRequest.setLastName("Doe");
        userRequest.setEmail("john.doe@example.com");
        userRequest.setPassword("password123");

        // Act & Assert
        bindToServer()
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/register")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(objectMapper.writeValueAsString(userRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Account created. Check your email to enable your account");

        assertThat(userRepository.findByEmailIgnoreCase("john.doe@example.com")).isPresent();
    }

    @Test
    void shouldLoginUserSuccessfully() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password123");

        // Act & Assert
        bindToServer()
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/login")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(objectMapper.writeValueAsString(loginRequest))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Login successful into controller.");
    }

    @Test
    void shouldVerifyAccountSuccessfully() throws Exception {
        // Arrange
        String verificationKey = "test-verification-key";

        // Act & Assert
        bindToServer()
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/verify/account").queryParam("key", verificationKey).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Account verified successfully.");
    }

    @Test
    void shouldRefreshTokensSuccessfully() throws Exception {
        // Arrange
        String refreshToken = "test-refresh-token";

        // Act & Assert
        bindToServer()
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/refresh")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Tokens refreshed successfully.");
    }
}
