package com.example.userservice.service;

import com.example.userservice.dto.*;
import com.example.userservice.entity.User;
import com.example.userservice.exception.EmailAlreadyInUseException;
import com.example.userservice.exception.PasswordChangeIllegalArgumentException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.exception.UsernameAlreadyTakenException;
import com.example.userservice.repo.UserRepository;
import com.example.userservice.security.AppUserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype .Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRqToUserConverter userRqToUserConverter;
    private final UserToUserRsConverter userToUserRsConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserRs> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userToUserRsConverter::convert).toList();
    }

    public UserRs findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userToUserRsConverter.convert(user);
    }

    @Transactional
    public UserRs createUser(UserRq rq) {
        validateUniqueFields(rq);
        User newUser = Optional.ofNullable(userRqToUserConverter.convert(rq))
                .orElseThrow(() -> new IllegalArgumentException("Conversion failed"));
        User savedUser = userRepository.save(newUser);
        return userToUserRsConverter.convert(savedUser);
    }

    private void validateUniqueFields(@NotNull UserRq rq) {
        if (userRepository.existsByUsername(rq.getUsername())) {
            throw new UsernameAlreadyTakenException("Username is already taken");
        }
        if (userRepository.existsByEmail(rq.getEmail())) {
            throw new EmailAlreadyInUseException("Email is already in use");
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public UserRs update(Long userId, UserRq rq) {
        User updateUser = Optional.ofNullable(userRqToUserConverter.convert(rq))
                .orElseThrow(() -> new IllegalArgumentException("Conversion failed"));
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        validateUsernameAndEmailForUpdate(existingUser, updateUser);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities()
                .stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_admin"))) {
            existingUser.setUsername(updateUser.getUsername());
        }else {
            existingUser.setUsername(updateUser.getUsername());
            existingUser.setEmail(updateUser.getEmail());
            existingUser.setEnabled(updateUser.isEnabled());
            existingUser.setRoles(updateUser.getRoles());
        }

        User savedUser = userRepository.save(existingUser);
        return userToUserRsConverter.convert(savedUser);
    }

    private void validateUsernameAndEmailForUpdate(@NotNull User existingUser, @NotNull User updateUser) {
        if (!existingUser.getUsername().equals(updateUser.getUsername()) &&
                userRepository.existsByUsername(updateUser.getUsername())) {
            throw new UsernameAlreadyTakenException("Username is already taken");
        }
        if (!existingUser.getEmail().equals(updateUser.getEmail()) &&
                userRepository.findByEmail(updateUser.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email is already in use");
        }
    }

    @Transactional
    public void deleteById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.delete(user);
    }

    public void changePassword(Long userId, @NotNull PasswordRq rq) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(
                MessageFormatter.format("User with id {} not found", userId).getMessage()));

        if (!passwordEncoder.matches(rq.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        if (!rq.getNewPassword().equals(rq.getConfirmNewPassword())) {
            throw new PasswordChangeIllegalArgumentException("New password and confirm new password do not match");
        }

        user.setPassword(passwordEncoder.encode(rq.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .map(AppUserPrincipal::new)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));
    }

}


