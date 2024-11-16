package com.example.userservice.dto;

import com.example.userservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRqToUserConverter implements Converter<UserRq, User> {


    private final PasswordEncoder passwordEncoder;

    @Override
    public User convert(@NotNull UserRq rq) {

        return User.builder()
                .username(rq.getUsername())
                .email(rq.getEmail())
                .password(passwordEncoder.encode(rq.getPassword()))
                .roles(rq.getRoles())
                .enabled(rq.isEnabled())
                .build();

    }
}
