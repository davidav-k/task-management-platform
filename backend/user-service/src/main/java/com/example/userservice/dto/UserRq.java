package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public  class UserRq{


        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username;

        @Email(message = "The email address must be in the format user@example.com")
        String email;

        // TODO police
        @Size(min = 8, max = 255, message = "The password length must be from 8 no more than 255 characters.")
        String password;

        List<String> roles;

        boolean enable;
}
