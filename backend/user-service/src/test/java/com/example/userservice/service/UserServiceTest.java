package com.example.userservice.service;


import com.example.userservice.dto.UserRq;
import com.example.userservice.dto.UserRqToUserConverter;
import com.example.userservice.dto.UserRs;
import com.example.userservice.dto.UserToUserRsConverter;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.RoleType;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.repo.RoleRepository;
import com.example.userservice.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Autowired
    UserService userService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    RoleRepository roleRepository;
    @MockBean
    private UserToUserRsConverter userToUserRsConverter;
    @MockBean
    private UserRqToUserConverter userRqToUserConverter;
    @MockBean
    private PasswordEncoder passwordEncoder;


    @Test
    void createUserSuccess() {
        Role roleAdmin = Role.builder().name(RoleType.ROLE_ADMIN).build();
        User user = User.builder().id(1L).username("admin").email("admin@mail.com").password("password")
                .roles(Set.of(roleAdmin)).enabled(true).build();
        UserRs rs = UserRs.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .roles(List.of())
                .isEnabled(true)
                .build();
        UserRq rq = UserRq.builder()
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles(List.of("ROLE_ADMIN"))
                .build();

        given(userRqToUserConverter.convert(any(UserRq.class))).willReturn(user);
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userToUserRsConverter.convert(any(User.class))).willReturn(rs);

        UserRs response = userService.createUser(rq);

        assertNotNull(response);
        assertEquals("admin", response.getUsername());
        assertEquals("admin@mail.com", response.getEmail());
        assertTrue(response.isEnabled());
    }

    @Test
    void createUserEmptyUsernameFail() {
        Role roleAdmin = Role.builder().name(RoleType.ROLE_ADMIN).build();
        User user = User.builder().id(1L).username("admin").email("admin@mail.com").password("password")
                .roles(Set.of(roleAdmin)).enabled(true).build();
        UserRq rq = UserRq.builder().username("admin").email("admin@mail.com").password("password")
                .roles(List.of("ROLE_ADMIN")).build();
        given(userRqToUserConverter.convert(any(UserRq.class))).willReturn(user);
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(rq));
    }

    @Test
    void createUserEmptyEmailFail() {
        Role roleAdmin = Role.builder().name(RoleType.ROLE_ADMIN).build();
        User user = User.builder().id(1L).username("admin").email("admin@mail.com").password("password")
                .roles(Set.of(roleAdmin)).enabled(true).build();
        UserRq rq = UserRq.builder().username("admin").email("admin@admin.com").password("password")
                .roles(List.of("ROLE_ADMIN")).build();
        given(userRqToUserConverter.convert(any(UserRq.class))).willReturn(user);
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(rq));
    }


    @Test
    void findByIdSuccess() {
        User user = User.builder().id(1L).build();
        UserRs rs = UserRs.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .roles(List.of("ROLE_ADMIN"))
                .isEnabled(true)
                .build();
        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
        given(userToUserRsConverter.convert(any(User.class))).willReturn(rs);

        UserRs foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals("admin", foundUser.getUsername());
        assertEquals("admin@mail.com", foundUser.getEmail());
    }

    @Test
    void findByIdFail() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void findByUsernameSuccess() {
        User user = User.builder().id(1L).build();
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));

        User UserFromDB = userService.findByUsername("admin");
        assertNotNull(UserFromDB);

    }

    @Test
    void findByUsernameFail() {
        User user = User.builder().id(1L).build();
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername(""));
    }

    @Test
    void findByEmailSuccess() {
        User user = User.builder().id(1L).email("admin@mail.com").build();
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));
        User UserFromDB = userService.findByEmail("admin@mail.com");
        assertNotNull(UserFromDB);
    }

    @Test
    void findByEmailFail() {
        User user = User.builder().id(1L).build();
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(""));

    }

    @Test
    void findAllSuccess() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        User user3 = User.builder().id(3L).build();
        List<User> users = new ArrayList<>(List.of(user1, user2, user3));
        UserRs userRs1 = UserRs.builder().id(1L).build();
        given(userRepository.findAll()).willReturn(users);
        given(userToUserRsConverter.convert(any(User.class))).willReturn(userRs1);

        List<UserRs> rs = userService.findAll();

        assertNotNull(rs);
        assertEquals(3, rs.size());
    }

    @Test
    void updateUserSuccess() {
        Role roleAdmin = Role.builder().name(RoleType.ROLE_ADMIN).build();
        User user = User.builder().id(1L).username("admin").email("admin@mail.com").password("password")
                .roles(Set.of(roleAdmin)).enabled(true).build();
        UserRq rq = UserRq.builder().username("adminUp").email("admin@mail.com").password("password")
                .roles(List.of("ROLE_ADMIN")).build();
        UserRs rs = UserRs.builder().id(1L).username("adminUp").email("admin@mail.com")
                .roles(List.of("ROLE_ADMIN")).isEnabled(true).build();
        given(userRqToUserConverter.convert(any(UserRq.class))).willReturn(user);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.encode(anyString())).willReturn("password");
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userToUserRsConverter.convert(any(User.class))).willReturn(rs);

        UserRs savedUser = userService.update(1L, rq);

        assertEquals(rq.getUsername(), savedUser.getUsername());
        assertEquals(rq.getEmail(),savedUser.getEmail());
    }

    @Test
    void updateUserFail() {
        UserRq rq = UserRq.builder().build();
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.update(1L, rq);
        });
    }

    @Test
    void deleteByIdUserSuccess() {
        User user = User.builder().id(1L).build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        userService.deleteById(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteByIdUserFail() {

        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteById(1L);
        });
    }

    @Test
    void assignRoleToUserSuccess() {
        Role roleAdmin = Role.builder().name(RoleType.ROLE_ADMIN).build();
        Role roleUser = Role.builder().name(RoleType.ROLE_USER).build();
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles(roles)
                .enabled(true)
                .build();

        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
        given(roleRepository.findByName(any(RoleType.class))).willReturn(Optional.of(roleAdmin));
        given(userRepository.save(any(User.class))).willReturn(user);

        userService.assignRoleToUser("admin", "ROLE_ADMIN");

        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(roleRepository, times(1)).findByName(RoleType.ROLE_ADMIN);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void assignRoleToUserUserNotFoundFail() {

        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.assignRoleToUser("admin", "ROLE_ADMIN"));
    }

    @Test
    void assignRoleToUserRoleNotFoundFail() {
        User user = User.builder().id(1L).build();

        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));
        given(roleRepository.findByName(any(RoleType.class))).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.assignRoleToUser("admin", "ROLE_ADMIN"));
    }


}