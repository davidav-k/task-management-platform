package com.example.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    @Column(nullable = false, unique = true, updatable = false)
    private String userId;

    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

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

