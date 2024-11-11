package com.example.userservice.controller;

import com.example.userservice.dto.StatusCode;
import com.example.userservice.dto.UserRq;
import com.example.userservice.dto.UserRs;
import com.example.userservice.security.UserDetailsServiceImpl;
import com.example.userservice.security.jwt.JwtUtils;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserService userService;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void createUser_ShouldReturnSuccess() throws Exception {
//        UserRq rq = UserRq.builder()
//                .username("admin")
//                .email("admin@mail.com")
//                .password("password")
//                .roles(List.of("ROLE_ADMIN"))
//                .build();
//
//        when(userService.createUser(any(UserRq.class))).thenReturn(any(UserRs.class));
//
//        mockMvc.perform(post(baseUrl + "/user")
//                                .accept(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(rq))
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.timestamp").isNotEmpty())
//                .andExpect(jsonPath("$.flag").value(true))
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("User created successfully"));
//    }
//
////    @Test
//    @WithMockUser(roles = "ADMIN")
//    void findById_ShouldReturnUser() throws Exception {
//        UserRs rs = UserRs.builder()
//                .id(1L)
//                .username("admin")
//                .email("admin@mail.com")
//                .roles(List.of("ROLE_ADMIN"))
//                .isEnabled(true)
//                .build();
//
//        when(userService.findById(anyLong())).thenReturn(rs);
//
//        mockMvc.perform(get(baseUrl +"/user/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.statusCode").value(StatusCode.SUCCESS))
//                .andExpect(jsonPath("$.message").value("Found one success"))
//                .andExpect(jsonPath("$.data.id").value(1L))
//                .andExpect(jsonPath("$.data.username").value("username"));
//    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void findAll_ShouldReturnUserList() throws Exception {
//        UserRs userRs1 = new UserRs(1L, "user1");
//        UserRs userRs2 = new UserRs(2L, "user2");
//        when(userService.findAll()).thenReturn(Arrays.asList(userRs1, userRs2));
//
//        mockMvc.perform(get("/api/user"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.statusCode").value(StatusCode.SUCCESS))
//                .andExpect(jsonPath("$.message").value("Found all"))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data[0].username").value("user1"))
//                .andExpect(jsonPath("$.data[1].username").value("user2"));
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    void update_ShouldReturnUpdatedUser() throws Exception {
//        UserRq userRq = new UserRq("updatedUsername", "newPassword", "ROLE_USER");
//        UserRs userRs = new UserRs(1L, "updatedUsername");
//        when(userService.update(any(Long.class), any(UserRq.class))).thenReturn(userRs);
//
//        mockMvc.perform(put("/api/user/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userRq))) // Используем ObjectMapper
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.statusCode").value(StatusCode.SUCCESS))
//                .andExpect(jsonPath("$.message").value("Update success"))
//                .andExpect(jsonPath("$.data.username").value("updatedUsername"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void deleteById_ShouldReturnSuccess() throws Exception {
//        doNothing().when(userService).deleteById(1L);
//
//        mockMvc.perform(delete("/api/user/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.statusCode").value(StatusCode.SUCCESS))
//                .andExpect(jsonPath("$.message").value("Delete success"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void assignRoleToUser_ShouldReturnSuccess() throws Exception {
//        String roleName = "ROLE_ADMIN";
//        doNothing().when(userService).assignRoleToUser("username", roleName);
//
//        mockMvc.perform(post("/api/user/change-role/username")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(roleName))) // Используем ObjectMapper
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.statusCode").value(StatusCode.SUCCESS))
//                .andExpect(jsonPath("$.message").value("Role assign success"));
//    }




    @Test
    void createUserAsAdminSuccess() throws Exception {

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
                .andExpect(jsonPath("$.message").value("User created successfully"));
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
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
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
                .andExpect(jsonPath("$.message").value("Delete success"));
    }

    @Test
    void deleteByIdFail() throws Exception {

        doThrow(new EntityNotFoundException("user not found")).when(userService).deleteById(1L);

        this.mockMvc.perform(delete(baseUrl + "/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("user not found"));
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
                .andExpect(jsonPath("$.message").value("Role assign success"));
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
                .andExpect(jsonPath("$.message").value("user not found"));
    }

}