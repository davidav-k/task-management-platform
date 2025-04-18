package com.example.user_service.utils;


import com.example.user_service.dto.User;
import com.example.user_service.entity.CredentialEntity;
import com.example.user_service.entity.RoleEntity;
import com.example.user_service.entity.UserEntity;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.user_service.constant.Constants.NINETY_DAYS;
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
                .mfa(false)
                .enabled(false)
                .loginAttempts(0)
                .qrCodeSecret(EMPTY)
                .phone(EMPTY)
                .bio(EMPTY)
                .imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
                .role(role)
                .build();
    }


    public static User fromUserEntity(UserEntity userEntity, RoleEntity role, CredentialEntity credentialEntity) {
        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        user.setLastLogin(userEntity.getLastLogin().toString());
        user.setCredentialsNonExpired(isCredentialNonExpired(credentialEntity));
        user.setCreatedAt(userEntity.getCreatedAt().toString());
        user.setUpdatedAt(userEntity.getUpdatedAt().toString());
        user.setRole(role.getName());
        user.setAuthorities(role.getAuthorities().getValue());
        return user;
    }

    public static boolean isCredentialNonExpired(CredentialEntity credentialEntity) {
        return credentialEntity.getUpdatedAt().plusDays(NINETY_DAYS).isAfter(LocalDateTime.now());
    }

//    @SneakyThrows
//    public static void copyNonNullProperties(Object source, Object destination) {
//        Class<?> clazz = source.getClass();
//        Field[] fields = clazz.getDeclaredFields();
//
//        for (Field field : fields){
//            field.setAccessible(true);
//            Object value = field.get(source);
//            if (value != null){
//                field.set(destination, value);
//            }
//        }
//    }

}
