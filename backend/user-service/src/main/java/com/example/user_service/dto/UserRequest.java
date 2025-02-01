package com.example.user_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


/**
 * Data Transfer Object (DTO) representing a request to create or update a user.
 *
 * <p>This class is used to capture user input for creating or updating user information.
 * It includes fields for personal information, contact details, and security credentials.
 * The fields are validated using annotations to ensure correctness before processing the request.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Getter} - Generates getter methods for all fields.</li>
 *   <li>{@code @Setter} - Generates setter methods for all fields.</li>
 *   <li>{@code @JsonIgnoreProperties(ignoreUnknown = true)} - Ignores unknown JSON properties during deserialization.</li>
 *   <li>Validation annotations like {@code @NotEmpty}, {@code @Email}, and {@code @Pattern} to enforce validation rules.</li>
 * </ul>
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {

    @NotEmpty(message = "First name cannot be empty or null")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty or null")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty or null")
    @Email(message = "Invalid email address")
    private String email;

    //todo: policy
    @NotEmpty(message = "Password cannot be empty or null")
    private String password;

    private String bio;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number")
    private String phone;
}

