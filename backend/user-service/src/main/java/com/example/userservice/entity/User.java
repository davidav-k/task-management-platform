package com.example.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "username is required")
    private String username;

    @NotEmpty(message = "email is required")
    @Email
    private String email;

    @NotEmpty(message = "password is required")
    private String password;

    private boolean enabled;

    private String roles; // Space separated string

}
