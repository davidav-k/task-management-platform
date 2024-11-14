package com.example.userservice.util;

import com.example.userservice.entity.User;
import com.example.userservice.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile("dev-h2")
public class DBDataInitializer implements CommandLineRunner {


    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) {
        User admin = User.builder()
                .username("admin")
                .password("password")
                .email("admin@mail.com")
                .roles("admin")
                .enabled(true)
                .build();
        User user = User.builder()
                .username("user")
                .password("password")
                .email("user@mail.com")
                .roles("user")
                .enabled(true)
                .build();
        userRepository.save(admin);
        userRepository.save(user);
    }
}
