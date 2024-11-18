package com.example.userservice.util;

import com.example.userservice.dto.UserRq;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile("dev-h2")
public class DBDataInitializer implements CommandLineRunner {


    private final UserService userService;

    @Override
    @Transactional
    public void run(String... args) {
        UserRq adminRq = UserRq.builder()
                .username("admin")
                .password("Password123")
                .email("admin@mail.com")
                .roles("admin")
                .enabled(true)
                .build();
        UserRq user1Rq = UserRq.builder()
                .username("user1")
                .password("Password123")
                .email("user1@mail.com")
                .roles("user")
                .enabled(true)
                .build();
        UserRq user2Rq = UserRq.builder()
                .username("user2")
                .password("Password123")
                .email("user2@mail.com")
                .roles("user")
                .enabled(true)
                .build();
        userService.createUser(adminRq);
        userService.createUser(user1Rq);
        userService.createUser(user2Rq);
    }
}
