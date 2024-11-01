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
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRqToUserConverter userRqToUserConverter;
    private final UserToUserRsConverter userToUserRsConverter;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRs createUser(UserRq rq) {

        User newUser = userRqToUserConverter.convert(rq);
        assert newUser != null;
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        if (newUser.getRoles() == null || newUser.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new IllegalArgumentException("Default role not found"));
            newUser.getRoles().add(defaultRole);
        }

        User savedUser = userRepository.save(newUser);

        return userToUserRsConverter.convert(savedUser);
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

    public UserRs update(Long userId, UserRq rq) {

        User updatedUser = userRqToUserConverter.convert(rq);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        assert updatedUser != null;
        validateUsernameAndEmailForUpdate(existingUser, updatedUser);
        updatePasswordIfProvided(existingUser, updatedUser);
        updateRolesIfProvided(existingUser, updatedUser);

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setEnabled(updatedUser.isEnabled());

        User savedUser = userRepository.save(existingUser);
        return userToUserRsConverter.convert(savedUser);
    }

    private void updateRolesIfProvided(User existingUser, @NotNull User updatedUser) {
        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            existingUser.setRoles(updatedUser.getRoles());
        }
    }

    private void updatePasswordIfProvided(User existingUser, @NotNull User updatedUser) {
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
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

    public void deleteById(Long userId){
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            userRepository.delete(user);
    }

    public void assignRoleToUser(String username, String roleName) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Role role = roleRepository.findByName(RoleType.valueOf(roleName))
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }
}

