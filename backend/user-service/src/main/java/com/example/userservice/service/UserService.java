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
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRqToUserConverter userRqToUserConverter;
    private final UserToUserRsConverter userToUserRsConverter;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.default.role}")
    String roleNameDefault;


    @Transactional
    public UserRs createUser(UserRq rq) {

        User newUser = Optional.ofNullable(userRqToUserConverter.convert(rq))
                .orElseThrow(() -> new IllegalArgumentException("Conversion failed"));

        validateUniqueFields(newUser);

        Role defaultRole = roleRepository.findByName(RoleType.valueOf(roleNameDefault))
                .orElseThrow(() -> new IllegalArgumentException("Default role not found"));
        newUser.addRoleIfNotExists(defaultRole);

        User savedUser = userRepository.save(newUser);
        return userToUserRsConverter.convert(savedUser);
    }

    private void validateUniqueFields(@NotNull User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyTakenException("Username is already taken");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email is already in use");
        }
    }

    public UserRs findById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        return userToUserRsConverter.convert(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<UserRs> findAll() {

        List<User> users = userRepository.findAll();

        return users.stream().map(userToUserRsConverter::convert).toList();
    }

    @Transactional
    public UserRs update(Long userId, UserRq rq) {

        User updatedUser = Optional.ofNullable(userRqToUserConverter.convert(rq))
                .orElseThrow(() -> new IllegalArgumentException("Conversion failed"));

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        validateUsernameAndEmailForUpdate(existingUser, updatedUser);
        existingUser.updateFields(updatedUser, passwordEncoder);

        User savedUser = userRepository.save(existingUser);
        return userToUserRsConverter.convert(savedUser);
    }

    private void validateUsernameAndEmailForUpdate(@NotNull User existingUser, @NotNull User updatedUser) {
        if (!existingUser.getUsername().equals(updatedUser.getUsername()) &&
                userRepository.findByUsername(updatedUser.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
    }

    @Transactional
    public void deleteById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Transactional
    public void assignRoleToUser(String username, String roleName) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Role role = roleRepository.findByName(RoleType.valueOf(roleName))
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        user.addRoleIfNotExists(role);
        userRepository.save(user);
    }
}


