package com.example.userservice.service;


import com.example.userservice.dto.UserRq;
import com.example.userservice.dto.UserRqToUserConverter;
import com.example.userservice.dto.UserRs;
import com.example.userservice.dto.UserToUserRsConverter;
import com.example.userservice.entity.User;
import com.example.userservice.exception.EmailAlreadyInUseException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.exception.UsernameAlreadyTakenException;
import com.example.userservice.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    private UserToUserRsConverter userToUserRsConverter;
    @Mock
    private UserRqToUserConverter userRqToUserConverter;
    @Mock
    private CommandLineRunner commandLineRunner; // don't load init data


    @Test
    void createUserSuccess() {
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles("admin")
                .enabled(true)
                .build();
        UserRq rq = UserRq.builder()
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles("admin")
                .enable(true)
                .build();
        UserRs rs = UserRs.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .roles("admin")
                .isEnabled(true)
                .build();

        given(userRqToUserConverter.convert(any(UserRq.class))).willReturn(user);
        given(userRepository.existsByUsername(anyString())).willReturn(false);
        given(userRepository.existsByEmail(anyString())).willReturn(false);
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
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles("admin")
                .enabled(true)
                .build();
        UserRq rq = UserRq.builder()
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles("admin")
                .build();
        given(userRepository.existsByUsername(anyString())).willReturn(true);

        assertThrows(UsernameAlreadyTakenException.class, () -> userService.createUser(rq));
    }

    @Test
    void createUserEmptyEmailFail() {
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles("admin")
                .enabled(true)
                .build();
        UserRq rq = UserRq.builder()
                .username("admin")
                .email("admin@admin.com")
                .password("password")
                .enable(true)
                .roles("admin")
                .build();
        given(userRepository.existsByUsername(anyString())).willReturn(false);
        given(userRepository.existsByEmail(anyString())).willReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> userService.createUser(rq));
    }

    @Test
    void findByIdSuccess() {
        User user = User.builder().id(1L).build();
        UserRs rs = UserRs.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .roles("admin")
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
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void findByUsernameSuccess() {
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles("admin")
                .enabled(true)
                .build();
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));

        User UserFromDB = userService.findByUsername("admin");

        assertNotNull(UserFromDB);
        assertEquals("admin", UserFromDB.getUsername());
        assertEquals("admin@mail.com", UserFromDB.getEmail());
        assertTrue(UserFromDB.isEnabled());

    }

    @Test
    void findByUsernameFail() {

        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername(""));
    }

    @Test
    void findByEmailSuccess() {
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles("admin")
                .enabled(true)
                .build();

        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

        User UserFromDB = userService.findByEmail("admin@mail.com");

        assertNotNull(UserFromDB);
        assertEquals("admin", UserFromDB.getUsername());
        assertEquals("admin@mail.com", UserFromDB.getEmail());
        assertTrue(UserFromDB.isEnabled());
    }

    @Test
    void findByEmailFail() {
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail("admin@mail.com"));

    }

    @Test
    void findAllSuccess() {
        User user1 = User.builder().id(1L).build();
        List<User> users = new ArrayList<>(List.of(user1, user1, user1));
        UserRs userRs1 = UserRs.builder().id(1L).build();
        given(userRepository.findAll()).willReturn(users);
        given(userToUserRsConverter.convert(any(User.class))).willReturn(userRs1);

        List<UserRs> rs = userService.findAll();

        assertNotNull(rs);
        assertEquals(3, rs.size());
    }

    @Test
    void updateSuccess() {
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles("admin")
                .enabled(true)
                .build();
        UserRq rq = UserRq.builder()
                .username("adminUp")
                .email("adminUp@mail.com")
                .password("password")
                .roles("admin")
                .enable(true)
                .build();
        UserRs rs = UserRs.builder()
                .id(1L)
                .username("adminUp")
                .email("adminUp@mail.com")
                .roles("admin")
                .isEnabled(true)
                .build();
        given(userRqToUserConverter.convert(any(UserRq.class))).willReturn(user);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userToUserRsConverter.convert(any(User.class))).willReturn(rs);

        UserRs savedUser = userService.update(1L, rq);

        assertEquals("adminUp", savedUser.getUsername());
        assertEquals("adminUp@mail.com",savedUser.getEmail());
    }

    @Test
    void updateFail() {
        UserRq rq = UserRq.builder().build();
        assertThrows(IllegalArgumentException.class, () -> {
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
}