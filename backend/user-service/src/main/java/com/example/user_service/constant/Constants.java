package com.example.user_service.constant;
/**
 * Utility class that defines application-wide constants used for roles, authorities,
 * and access control.
 *
 * <p>This class contains static final fields that are used throughout the application
 * for defining role prefixes, delimiters, and specific authorities for different user
 * roles (User, Manager, Admin, Super Admin). These constants are commonly used in
 * authentication and authorization logic.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     String roleWithPrefix = Constants.ROLE_PREFIX + "ADMIN";
 *     String userAuthorities = Constants.USER_AUTHORITIES;
 * </pre>
 */
public class Constants {

    /**
     * Prefix used to denote roles in the application (e.g., ROLE_USER, ROLE_ADMIN).
     */
    public static final String ROLE_PREFIX = "ROLE_";

    /**
     * Delimiter used to separate multiple authorities in a single string.
     */
    public static final String AUTHORITY_DELIMITER = ",";

    /**
     * Comma-separated list of authorities assigned to a standard user.
     *
     * <p>A user with this role can create, read, and update documents.</p>
     */
    public static final String USER_AUTHORITIES = "document:create, document:read, document:update";

    /**
     * Comma-separated list of authorities assigned to a manager.
     *
     * <p>A manager with this role can create, read, update, and delete documents.</p>
     */
    public static final String MANAGER_AUTHORITIES = "document:create, document:read, document:update, document:delete";

    /**
     * Comma-separated list of authorities assigned to an admin.
     *
     * <p>An admin with this role has user-related permissions (create, read, update users)
     * as well as document-related permissions (create, read, update, and delete documents).</p>
     */
    public static final String ADMIN_AUTHORITIES = "user:create, user:read, user:update, document:create, document:read, document:update, document:delete";

    /**
     * Comma-separated list of authorities assigned to a super admin.
     *
     * <p>A super admin has the highest level of permissions, including full user management
     * (create, read, update, delete users) and full document management
     * (create, read, update, delete documents).</p>
     */
    public static final String SUPER_ADMIN_AUTHORITIES = "user:create, user:read, user:update, user:delete, document:create, document:read, document:update, document:delete";
}

