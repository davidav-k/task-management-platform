package com.example.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Represents the User entity with information related to user accounts and security.
 *
 * <p>This class maps to the "users" table in the database and contains fields that
 * are essential for user authentication, authorization, and profile management.
 * It extends the Auditable class to inherit audit-related fields and logic.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Entity} - Specifies that this class is a JPA entity.</li>
 *   <li>{@code @Table(name = "users")} - Maps the class to the "users" table in the database.</li>
 *   <li>{@code @JsonInclude} - Excludes default values from the serialized JSON representation.</li>
 *   <li>{@code @Getter, @Setter, @ToString} - Lombok annotations to generate boilerplate methods.</li>
 * </ul>
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserEntity extends Auditable {

    /**
     * Unique identifier for the user.
     */
    @Column(nullable = false, unique = true, updatable = false)
    private String userId;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The email address of the user. This must be unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Number of failed login attempts by the user.
     */
    private Integer loginAttempts;

    /**
     * The date and time of the user's last login.
     */
    private LocalDate lastLogin;

    /**
     * The phone number of the user.
     */
    private String phone;

    /**
     * A short biography or description of the user.
     */
    private String bio;

    /**
     * URL of the user's profile image.
     */
    private String imageUrl;

    /**
     * Indicates whether the user's account is non-expired.
     */
    private boolean accountNonExpired;

    /**
     * Indicates whether the user's account is non-locked.
     */
    private boolean accountNonLocked;

    /**
     * Indicates whether the user's account is enabled.
     */
    private boolean enabled;

    /**
     * Indicates whether multi-factor authentication (MFA) is enabled for the user.
     */
    private boolean mfa;

    /**
     * The secret used for generating MFA QR codes. Excluded from JSON serialization.
     */
    @JsonIgnore
    private String qrCodeSecret;

    /**
     * The URL of the QR code image used for multi-factor authentication.
     */
    @Column(columnDefinition = "TEXT")
    private String qrCodeImageUrl;

    /**
     * The role assigned to the user.
     * This is a many-to-one relationship with the RoleEntity.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id")
    )
    private RoleEntity role;
}

