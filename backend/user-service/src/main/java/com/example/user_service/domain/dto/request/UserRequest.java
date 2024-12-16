package com.example.user_service.domain.dto.request;

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

    /**
     * The first name of the user.
     *
     * <p>This field is required and cannot be empty or null.</p>
     *
     * <p>Validation rules:</p>
     * <ul>
     *   <li>{@code @NotEmpty} - Ensures the field is not empty or null.</li>
     * </ul>
     */
    @NotEmpty(message = "First name cannot be empty or null")
    private String firstName;

    /**
     * The last name of the user.
     *
     * <p>This field is required and cannot be empty or null.</p>
     *
     * <p>Validation rules:</p>
     * <ul>
     *   <li>{@code @NotEmpty} - Ensures the field is not empty or null.</li>
     * </ul>
     */
    @NotEmpty(message = "Last name cannot be empty or null")
    private String lastName;

    /**
     * The email address of the user.
     *
     * <p>This field is required and must be a valid email address format.</p>
     *
     * <p>Validation rules:</p>
     * <ul>
     *   <li>{@code @NotEmpty} - Ensures the field is not empty or null.</li>
     *   <li>{@code @Email} - Ensures the email address is in a valid format.</li>
     * </ul>
     */
    @NotEmpty(message = "Email cannot be empty or null")
    @Email(message = "Invalid email address")
    private String email;

    /**
     * The password for the user account.
     *
     * <p>This field is required and cannot be empty or null. The password may be subject
     * to additional security constraints in the service layer.</p>
     *
     * <p>Validation rules:</p>
     * <ul>
     *   <li>{@code @NotEmpty} - Ensures the field is not empty or null.</li>
     * </ul>
     */
    @NotEmpty(message = "Password cannot be empty or null")
    private String password;

    /**
     * A short biography or description of the user.
     *
     * <p>This field is optional and can contain a brief bio or description of the user.</p>
     */
    private String bio;

    /**
     * The phone number of the user.
     *
     * <p>This field is optional but, if provided, must follow a valid phone number pattern.</p>
     *
     * <p>Validation rules:</p>
     * <ul>
     *   <li>{@code @Pattern} - Ensures the phone number matches the pattern {@code ^(\+\d{1,3}[- ]?)?\d{10}$}.</li>
     * </ul>
     */
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number")
    private String phone;
}

