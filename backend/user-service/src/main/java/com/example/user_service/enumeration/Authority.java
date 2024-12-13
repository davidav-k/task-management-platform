package com.example.user_service.enumeration;

import static com.example.user_service.constant.Constants.*;

/**
 * Enum representing different levels of authority in the system.
 *
 * <p>This enum defines the various roles that a user can have and associates
 * each role with a specific set of authorities or permissions. These roles can
 * be used to control access and permissions throughout the application.</p>
 *
 * <p>Each constant in this enum has an associated value that represents the specific
 * set of permissions assigned to the role.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     Authority userRole = Authority.USER;
 *     String permissions = userRole.getValue();
 * </pre>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Enum} - Represents a fixed set of constants.</li>
 * </ul>
 */
public enum Authority {

    USER(USER_AUTHORITIES),

    MANAGER(MANAGER_AUTHORITIES),

    ADMIN(ADMIN_AUTHORITIES),

    SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);

    /**
     * The value associated with the authority level, which represents the set of
     * permissions assigned to the role.
     */
    private final String value;

    Authority(String value) {
        this.value = value;
    }

    /**
     * Retrieves the value associated with this authority.
     *
     * @return the value representing the set of permissions for this authority
     */
    public String getValue() {
        return value;
    }
}
