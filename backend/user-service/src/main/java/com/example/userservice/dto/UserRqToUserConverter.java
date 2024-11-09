package com.example.userservice.dto;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.RoleType;
import com.example.userservice.entity.User;
import com.example.userservice.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
                .enabled(rq.enable)
                .build();
    }
}
