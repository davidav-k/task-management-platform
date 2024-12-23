package com.example.user_service.utils;

import com.example.user_service.entity.RoleEntity;
import com.example.user_service.entity.UserEntity;
import java.time.LocalDate;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Utility class for creating instances of {@code UserEntity} objects.
 *
 * <p>This class provides methods to simplify the creation of {@code UserEntity} instances
 * with pre-defined default values. It is used to construct new user entities for registration
 * and user management purposes.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     RoleEntity role = roleRepository.findByName("USER").orElseThrow();
 *     UserEntity user = UserUtils.createUserEntity("John", "Doe", "john.doe@example.com", role);
 * </pre>
 */
public class UserUtils {
    /**
     * Creates a new {@code UserEntity} with the specified details.
     *
     * <p>This method generates a new {@code UserEntity} with default values for fields like
     * login attempts, QR code secret, phone, bio, and profile image URL. The user ID is
     * generated using {@code UUID} to ensure uniqueness.</p>
     *
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address of the user
     * @param role the role assigned to the user
     * @return a new {@code UserEntity} instance with the provided details and default values
     */
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

}
