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
                .password("password")
                .email("admin@mail.com")
                .roles("admin")
                .enabled(true)
                .build();
        UserRq userRq = UserRq.builder()
                .username("user")
                .password("password")
                .email("user@mail.com")
                .roles("user")
                .enabled(true)
                .build();
        userService.createUser(adminRq);
        userService.createUser(userRq);
    }
}
