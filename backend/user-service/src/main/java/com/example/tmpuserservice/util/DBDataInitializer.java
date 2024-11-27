package com.example.tmpuserservice.util;

import com.example.tmpuserservice.dto.user.UserRq;
import com.example.tmpuserservice.dto.user.UserRs;
import com.example.tmpuserservice.entity.*;
import com.example.tmpuserservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("test")
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    @Transactional
    public void run(String... args) {

        UserRq adminRq = new UserRq("admin", "admin@mail.com", "Password123", Set.of(RoleType.ROLE_ADMIN), true);
        UserRq user1Rq = new UserRq("user1", "user1@mail.com", "Password123", Set.of(RoleType.ROLE_USER), true);
        UserRq user2Rq = new UserRq("user2", "user2@mail.com", "Password123", Set.of(RoleType.ROLE_USER), true);

        userService.create(adminRq);
        userService.create(user1Rq);
        userService.create(user2Rq);

    }
}
