package com.example.userservice.service;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.RoleType;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.repo.RoleRepository;
import com.example.userservice.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    PasswordEncoder passwordEncoder;


    @Test
    void createUserSuccess() {

        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles(Set.of(Role.builder().name(RoleType.ROLE_ADMIN).build()))
                .enabled(true)
                .build();
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
        given(passwordEncoder.encode(any(String.class))).willReturn("password");
        given(userRepository.save(user)).willReturn(user);

        User UserFromDB = userService.createUser(user);

        assertNotNull(UserFromDB);
        assertEquals("admin", UserFromDB.getUsername());
        assertEquals("admin@mail.com", UserFromDB.getEmail());
        assertEquals("password", UserFromDB.getPassword());
        assertTrue(UserFromDB.isEnabled());

    }

    @Test
    void createUserEmptyUsernameFail() {
        User user = User.builder().username("").build();
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    void createUserEmptyEmailFail() {
        User user = User.builder().username("admin").email("").build();
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }


    @Test
    void findByIdSuccess() {
        User user = User.builder().id(1L).build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        User UserFromDB = userService.findById(1L);
        assertNotNull(UserFromDB);
    }

    @Test
    void findByIdFail() {
        User user = User.builder().id(1L).build();
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
        given(userRepository.findAll()).willReturn(users);

        List<User> UserFromDB = userService.findAll();

        assertNotNull(UserFromDB);
        assertEquals(3, UserFromDB.size());

    }

    @Test
    void updateUserSuccess() {
        User user = User.builder().id(1L).build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

    }
@Test
void updateUserFail() {
        User user = User.builder().id(1L).build();
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(1L, user);
        });
}
    @Test
    void deleteUserSuccess() {
        User user = User.builder().id(1L).build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        userService.deleteUser(1L);

        assertNull(userService.findById(1L));
    }
    @Test
    void deleteUserFail() {

    }

    @Test
    void assignRoleToUser() {
    }
}