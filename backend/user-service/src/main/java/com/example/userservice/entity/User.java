package com.example.userservice.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "username must be filled")
    private String username;

    @NotEmpty(message = "email must be filled")
    @Email
    private String email;

    @NotEmpty(message = "password must be filled")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    private boolean enabled;

    public void updateFields(User updatedUser, PasswordEncoder passwordEncoder) {
        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isBlank()){
            this.username = updatedUser.getUsername();
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank()){
            this.username = updatedUser.getEmail();
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            this.password = passwordEncoder.encode(updatedUser.getPassword());
        }

        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            this.roles = updatedUser.getRoles();
        }
    }

    public void addRoleIfNotExists(Role role) {
        if (this.roles.stream().noneMatch(r -> r.getName().equals(role.getName()))) {
            this.roles.add(role);
        }
    }


}


