package com.example.userservice.util;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.RoleType;
import com.example.userservice.entity.User;
import com.example.userservice.repo.RoleRepository;
import com.example.userservice.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("dev-h2")
public class DBDataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) {
        Role roleAdmin = Role.builder().name(RoleType.ROLE_ADMIN).build();
        Role roleUser = Role.builder().name(RoleType.ROLE_USER).build();
        roleAdmin = roleRepository.save(roleAdmin);
        roleUser = roleRepository.save(roleUser);
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("passadmin"))
                .email("admin@mail.com")
                .roles(Set.of(roleAdmin))
                .enabled(true)
                .build();
        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("passuser"))
                .email("user@mail.com")
                .roles(Set.of(roleUser))
                .enabled(true)
                .build();
        userRepository.save(admin);
        userRepository.save(user);
    }
}
