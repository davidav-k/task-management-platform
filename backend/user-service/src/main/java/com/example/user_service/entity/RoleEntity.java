package com.example.user_service.entity;


import com.example.user_service.enumeration.Authority;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Represents the Role entity that stores role-related information
 * associated with a specific user or group of users.
 *
 * <p>This class maps to the "roles" table in the database and contains information
 * related to role names and authorities. It extends the Auditable class to inherit
 * audit-related fields and logic.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Entity} - Specifies that this class is a JPA entity.</li>
 *   <li>{@code @Table(name = "roles")} - Maps the class to the "roles" table in the database.</li>
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
@Table(name = "roles")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RoleEntity extends Auditable {

    private String name;

    /**
     * These authorities determine what actions a user with this role can perform.</p>
     */
    private Authority authorities;
}

