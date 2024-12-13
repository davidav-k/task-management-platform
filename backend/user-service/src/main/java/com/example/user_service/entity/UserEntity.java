package com.example.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Represents the User entity with information related to user accounts and security.
 * This class maps to the "users" table in the database and contains fields that
 * are essential for user authentication, authorization, and profile management.
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

    private String firstName;

    private String lastName;

    /**
     * This must be unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Number of failed login attempts by the user.
     */
    private Integer loginAttempts;

    private LocalDate lastLogin;

    private String phone;

    private String bio;

    private String imageUrl;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean enabled;

    private boolean mfa;

    @JsonIgnore
    private String qrCodeSecret;

    /**
     * The URL of the QR code image used for multifactorial authentication.
     */
    @Column(columnDefinition = "text")
    private String qrCodeImageUrl;

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

