package com.example.user_service.service;

import com.example.user_service.entity.RoleEntity;

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
     * and password. The created user may be stored in a database or an external
     * system depending on the implementation.</p>
     *
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address of the user
     * @param password the password for the user account
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
}

