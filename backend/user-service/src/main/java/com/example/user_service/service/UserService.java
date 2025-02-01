package com.example.user_service.service;

import com.example.user_service.domain.ApiAuthentication;
import com.example.user_service.dto.User;
import com.example.user_service.entity.CredentialEntity;
import com.example.user_service.entity.RoleEntity;
import com.example.user_service.entity.UserEntity;
import com.example.user_service.enumeration.LoginType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.core.Authentication;

/**
 * Service interface for handling user-related operations.
 *
 * <p>This interface defines methods for managing user accounts, such as creating
 * a new user and retrieving role information. Implementations of this interface
 * are responsible for the actual business logic for user management.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     userService.createUser("John", "Doe", "john.doe@example.com", "password123");
 *     RoleEntity role = userService.getRoleName("ADMIN");
 * </pre>
 */
public interface UserService {

    /**
     * Creates a new user with the specified details.
     *
     * <p>This method creates a user with the provided first name, last name, email,
     * and password. The created user stored in a database</p>
     */
    void createUser(String firstName, String lastName, String email, String password);

    /**
     * Retrieves a RoleEntity by its name.
     *
     * <p>This method finds and returns a RoleEntity that matches the provided role name.</p>
     *
     * @param name the name of the role to search for
     * @return the RoleEntity corresponding to the specified role name
     */
    RoleEntity getRoleName(String name);

    void verifyAccountKey(String key);

    void updateLoginAttempt(String email, LoginType loginType);

    User getUserByUserId(String apply);

    User getUserByEmail(String email);

    UserEntity getUserEntityByEmail(String email);

    CredentialEntity getUserCredentialById(Long id);

    ApiAuthentication authenticateUser(String email, String password);
}

