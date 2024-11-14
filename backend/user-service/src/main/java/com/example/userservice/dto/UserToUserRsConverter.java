package com.example.userservice.dto;

import com.example.userservice.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserRsConverter implements Converter<User, UserRs> {

    @Override
    public UserRs convert(@NotNull User user) {

        return UserRs.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isEnabled(user.isEnabled())
                .build();
    }
}
