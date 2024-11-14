package com.example.userservice.dto;

import com.example.userservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRqToUserConverter implements Converter<UserRq, User> {

    @Override
    public User convert(@NotNull UserRq rq) {

        return User.builder()
                .username(rq.getUsername())
                .email(rq.getEmail())
                .password(rq.getPassword())
                .roles(rq.getRoles())
                .enabled(rq.isEnable())
                .build();

    }
}
