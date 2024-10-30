package com.example.userservice.service;

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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public User createUser(@NotNull User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserNotFoundException("Username is already taken");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserNotFoundException("Email is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new EntityNotFoundException("Default role not found"));
            user.getRoles().add(defaultRole);
        }

        user.setEnabled(true);

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User updateUser(Long userId, User updatedUser) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        validateUsernameAndEmailForUpdate(existingUser, updatedUser);
        updatePasswordIfProvided(existingUser, updatedUser);
        updateRolesIfProvided(existingUser, updatedUser);

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setEnabled(updatedUser.isEnabled());

        return userRepository.save(existingUser);
    }

    private void updateRolesIfProvided(User existingUser, User updatedUser) {
        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            existingUser.setRoles(updatedUser.getRoles());
        }
    }

    private void updatePasswordIfProvided(User existingUser, User updatedUser) {
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

    public void deleteUser(Long userId){
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            userRepository.delete(user);
    }

    public void assignRoleToUser(String username, RoleType roleType) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }
}

