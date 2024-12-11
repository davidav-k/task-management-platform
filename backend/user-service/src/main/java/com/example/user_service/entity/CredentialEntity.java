package com.example.user_service.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Represents the Credential entity that stores sensitive authentication information
 * associated with a specific user.
 *
 * <p>This class maps to the "credentials" table in the database and contains information
 * related to user passwords and links to the corresponding User entity. It extends the
 * Auditable class to inherit audit-related fields and logic.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Entity} - Specifies that this class is a JPA entity.</li>
 *   <li>{@code @Table(name = "credentials")} - Maps the class to the "credentials" table in the database.</li>
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
@Table(name = "credentials")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CredentialEntity extends Auditable {

    /**
     * The password associated with the user.
     *
     * <p>This field stores the user's password and may be encrypted before being stored
     * in the database.</p>
     */
    private String password;

    /**
     * The UserEntity associated with this credential.
     *
     * <p>This is a one-to-one relationship with the UserEntity. The field is
     * linked to the UserEntity through the user_id column in the credentials table.
     * When the associated user is deleted, this credential is also deleted due
     * to the cascading delete rule.</p>
     *
     * <p>Annotations used:</p>
     * <ul>
     *   <li>{@code @OneToOne} - Establishes a one-to-one relationship with the UserEntity.</li>
     *   <li>{@code @JoinColumn} - Specifies the name of the column (user_id) that links
     *       this entity to the UserEntity.</li>
     *   <li>{@code @OnDelete(action = OnDeleteAction.CASCADE)} - Ensures that when the
     *       linked UserEntity is deleted, the corresponding CredentialEntity is also deleted.</li>
     *   <li>{@code @JsonIdentityInfo} - Prevents infinite recursion during serialization
     *       by referencing the user by its identifier (id) instead of serializing the entire UserEntity object.</li>
     *   <li>{@code @JsonIdentityReference} - Ensures that only the identifier (id) of the
     *       UserEntity is included in the serialized JSON, not the full UserEntity object.</li>
     *   <li>{@code @JsonProperty("user_id")} - Renames the userEntity field to user_id in JSON responses.</li>
     * </ul>
     */
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("user_id")
    private UserEntity userEntity;

    /**
     * Constructs a new CredentialEntity with the specified UserEntity and password.
     *
     * @param userEntity the associated UserEntity for this credential
     * @param password the user's password to be stored
     */
    public CredentialEntity(UserEntity userEntity, String password) {
        this.userEntity = userEntity;
        this.password = password;
    }
}
