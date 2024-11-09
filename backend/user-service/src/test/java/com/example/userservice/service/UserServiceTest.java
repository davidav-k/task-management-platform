package com.example.userservice.service;


import com.example.userservice.dto.UserRq;
import com.example.userservice.dto.UserRqToUserConverter;
import com.example.userservice.dto.UserRs;
import com.example.userservice.dto.UserToUserRsConverter;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.RoleType;
import com.example.userservice.entity.User;
import com.example.userservice.exception.EmailAlreadyInUseException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.exception.UsernameAlreadyTakenException;
import com.example.userservice.repo.RoleRepository;
import com.example.userservice.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    private UserToUserRsConverter userToUserRsConverter;
    @Mock
    private UserRqToUserConverter userRqToUserConverter;
    @Mock
    private PasswordEncoder passwordEncoder;

    Role roleAdmin;
    User user;
    UserRq rq;
    UserRs rs;

    @BeforeEach
    public void setup() {
        roleAdmin = Role.builder().name(RoleType.ROLE_ADMIN).build();
        user = User.builder().id(1L).username("admin").email("admin@mail.com").password("password")
                .roles(Set.of(roleAdmin)).enabled(true).build();
        rq = UserRq.builder()
                .username("admin")
                .email("admin@mail.com")
                .password("password")
                .roles(List.of("ROLE_ADMIN"))
                .enable(true)
                .build();
        rs = UserRs.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .roles(List.of("ROLE_ADMIN"))
                .isEnabled(true)
                .build();
    }


    @Test
    void createUserSuccess() {

        when(userRqToUserConverter.convert(any(UserRq.class))).thenReturn(user);
        when(roleRepository.findByName(any(RoleType.class))).thenReturn(Optional.of(roleAdmin));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userToUserRsConverter.convert(any(User.class))).thenReturn(rs);

        UserRs response = userService.createUser(rq);

        assertNotNull(response);
        assertEquals("admin", response.getUsername());
        assertEquals("admin@mail.com", response.getEmail());
        assertEquals("ROLE_ADMIN", response.getRoles().get(0));
        assertTrue(response.isEnabled());
    }

    @Test
    void createUserWithAlreadyUsedUsernameFail() {
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);

        assertThrows(UsernameAlreadyTakenException.class, () -> userService.createUser(rq));
    }

    @Test
    void createUserWithAlreadyUsedEmailFail() {
        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> userService.createUser(rq));
    }

    @Test
    void findByIdSuccess() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userToUserRsConverter.convert(any(User.class))).thenReturn(rs);

        UserRs foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals("admin", foundUser.getUsername());
        assertEquals("admin@mail.com", foundUser.getEmail());
    }

    @Test
    void findByIdFail() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void findByUsernameSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User response = userService.findByUsername("admin");
        assertNotNull(response);
        assertEquals("admin", response.getUsername());
        assertEquals("admin@mail.com", response.getEmail());
        assertTrue(response.isEnabled());

    }

    @Test
    void findByUsernameFail() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername(""));
    }

    @Test
    void findByEmailSuccess() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        User response = userService.findByEmail("admin@mail.com");
        assertNotNull(response);
        assertEquals("admin", response.getUsername());
        assertEquals("admin@mail.com", response.getEmail());
        assertTrue(response.isEnabled());
    }

    @Test
    void findByEmailFail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(""));

    }

    @Test
    void findAllSuccess() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        User user3 = User.builder().id(3L).build();
        List<User> users = new ArrayList<>(List.of(user1, user2, user3));
        UserRs userRs1 = UserRs.builder().id(1L).build();
        when(userRepository.findAll()).thenReturn(users);
        when(userToUserRsConverter.convert(any(User.class))).thenReturn(userRs1);

        List<UserRs> rs = userService.findAll();

        assertNotNull(rs);
        assertEquals(3, rs.size());
    }

    @Test
    void updateSuccess() {

        when(userRqToUserConverter.convert(any(UserRq.class))).thenReturn(user);
        when(roleRepository.findByName(any(RoleType.class))).thenReturn(Optional.of(roleAdmin));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userToUserRsConverter.convert(any(User.class))).thenReturn(rs);

        UserRs savedUser = userService.update(1L, rq);

        assertEquals("admin", savedUser.getUsername());
        assertEquals("admin@mail.com", savedUser.getEmail());
    }

    @Test
    void updateFail() {
        when(userRqToUserConverter.convert(any(UserRq.class))).thenReturn(user);
        when(roleRepository.findByName(any(RoleType.class))).thenReturn(Optional.of(roleAdmin));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.update(1L, rq);
        });
    }

    @Test
    void deleteByIdUserSuccess() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteById(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteByIdUserFail() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteById(1L);
        });
    }

    @Test
    void assignRoleToUserSuccess() {
        Role roleUser = Role.builder().name(RoleType.ROLE_USER).build();
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        user.setRoles(roles);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(any(RoleType.class))).thenReturn(Optional.of(roleAdmin));
        when(userRepository.save(any(User.class))).thenReturn(user);

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

        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));
        given(roleRepository.findByName(any(RoleType.class))).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.assignRoleToUser("admin", "ROLE_ADMIN"));
    }


}