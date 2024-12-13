package com.example.user_service.utils;

import com.example.user_service.entity.RoleEntity;
import com.example.user_service.entity.UserEntity;
import java.time.LocalDate;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.EMPTY;


public class UserUtils {

    public static UserEntity createUserEntity(String firstName, String lastName, String email, RoleEntity role){
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastLogin(LocalDate.now())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .loginAttempts(0)
                .qrCodeSecret(EMPTY)
                .phone(EMPTY)
                .bio(EMPTY)
                .imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
                .role(role)
                .build();
    }

}
