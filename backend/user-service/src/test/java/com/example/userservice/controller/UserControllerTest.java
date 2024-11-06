package com.example.userservice.controller;

import com.example.userservice.dto.StatusCode;
import com.example.userservice.dto.UserRq;
import com.example.userservice.dto.UserRs;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "dev-h2")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Value("${api.endpoint.base-url}")
    String baseUrl;


    @Test
    void createUserSuccess() throws Exception {

        UserRs rs = UserRs.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .roles(List.of("ROLE_ADMIN"))
                .isEnabled(true)
                .build();
        UserRq rq = UserRq.builder()
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles(List.of("ROLE_ADMIN"))
                .build();

        given(userService.createUser(any(UserRq.class))).willReturn(rs);

        mockMvc.perform(
                        post(baseUrl + "/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rq))
                                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.username").value("admin"))
                .andExpect(jsonPath("$.data.email").value("admin@mail.com"))
                .andExpect(jsonPath("$.data.roles").isArray())
                .andExpect(jsonPath("$.data.roles[0]").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void createUserFail() throws Exception {
        UserRq rq = UserRq.builder().username("").email("").password("").roles(List.of()).build();
        mockMvc.perform(
                post(baseUrl + "/user")
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

    @Test
    void findUserByIdSuccess() throws Exception {
        UserRs rs = UserRs.builder()
                .id(1L)
                .username("admin")
                .email("admin@admin.com")
                .roles(List.of("ROLE_ADMIN"))
                .isEnabled(true)
                .build();
        given(userService.findById(anyLong())).willReturn(rs);

        mockMvc.perform(get(baseUrl + "/user/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").value(Matchers.any(String.class)))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Found one success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("admin"))
                .andExpect(jsonPath("$.data.email").value("admin@admin.com"))
                .andExpect(jsonPath("$.data.roles").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void findUserByIdFail() throws Exception {

        given(userService.findById(anyLong())).willThrow(new EntityNotFoundException("User with id 3 not found"));

        mockMvc.perform(get(baseUrl + "/user/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("User with id 3 not found"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    void findAllSuccess() throws Exception {
        UserRs userRs1 = UserRs.builder().id(1L).build();
        UserRs userRs2 = UserRs.builder().id(2L).build();
        UserRs userRs3 = UserRs.builder().id(3L).build();
        List<UserRs> rs = new ArrayList<>(List.of(userRs1, userRs2, userRs3));
        given(userService.findAll()).willReturn(rs);

        mockMvc.perform(get(baseUrl + "/user").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found all"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.size()").value(3))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2));
    }

    @Test
    void updateSuccess() throws Exception {
        UserRs rs = UserRs.builder()
                .id(1L)
                .username("adminUp")
                .email("admin@mail.com")
                .roles(List.of("ROLE_ADMIN"))
                .isEnabled(true)
                .build();
        UserRq rq = UserRq.builder()
                .username("adminUp")
                .email("admin@mail.com")
                .password("password")
                .roles(List.of("ROLE_ADMIN"))
                .build();

        given(userService.update(anyLong(),any(UserRq.class))).willReturn(rs);


        this.mockMvc.perform(put(baseUrl + "/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update success"))
                .andExpect(jsonPath("$.data.username").value("adminUp"));
    }

    @Test
    void updateFail() throws Exception {

        UserRq rq = UserRq.builder()
                .username("")
                .email("")
                .password("")
                .roles(List.of())
                .build();

        this.mockMvc.perform(put(baseUrl + "/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are not valid"))
                .andExpect(jsonPath("$.data.password").value("The password length must be from 8 no more than 255 characters."))
                .andExpect(jsonPath("$.data.username").value("Username must be between 3 and 20 characters"));
    }

    @Test
    void deleteByIdSuccess() throws Exception {

        doNothing().when(userService).deleteById(1L);

        this.mockMvc.perform(delete(baseUrl + "/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete success"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void deleteByIdFail() throws Exception {

        doThrow(new EntityNotFoundException("user not found")).when(userService).deleteById(1L);

        this.mockMvc.perform(delete(baseUrl + "/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("user not found"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void  assignRoleToUserSuccess() throws Exception {

        doNothing().when(userService).assignRoleToUser(anyString(),anyString());

        this.mockMvc.perform(post(baseUrl + "/user/change-role/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("ROLE_USER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Role assign success"))
                .andExpect(jsonPath("$.data").isEmpty());

    }
    @Test
    void  assignRoleToUserFail() throws Exception {

        doThrow(new EntityNotFoundException("user not found")).when(userService).assignRoleToUser(anyString(),anyString());

        this.mockMvc.perform(post(baseUrl + "/user/change-role/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("ROLE_USER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("user not found"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}