package com.example.userservice.controller;

import com.example.userservice.dto.UserRegistrationRequest;
import com.example.userservice.dto.UserRs;
import com.example.userservice.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev-h2")
class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Test
    void registerUserSuccess() throws Exception {
        UserRegistrationRequest rq = UserRegistrationRequest.builder()
                .username("user")
                .password("password")
                .email("user@email.com")
                .build();
        UserRs rs = UserRs.builder()
                .id(1L)
                .username("user")
                .email("user@mail.com")
                .roles(List.of("ROLE_USER"))
                .isEnabled(true)
                .build();
        given(authService.register(rq)).willReturn(rs);

        mockMvc.perform(
                        post(baseUrl + "/auth/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rq))
                                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.username").value("user"))
                .andExpect(jsonPath("$.data.email").value("user@mail.com"))
                .andExpect(jsonPath("$.data.roles").isArray())
                .andExpect(jsonPath("$.data.roles[0]").value("ROLE_USER"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void registerUserFail() throws Exception {
        UserRegistrationRequest rq = UserRegistrationRequest.builder()
                .username("")
                .password("")
                .email("")
                .build();

        mockMvc.perform(
                        post(baseUrl + "/auth/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rq))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are not valid"))
                .andExpect(jsonPath("$.data.password").value("The password length must be from 8 no more than 255 characters."))
                .andExpect(jsonPath("$.data.username").value("Username must be between 3 and 20 characters"));
    }
}