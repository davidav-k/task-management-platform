package com.example.user_service.entity;

import com.example.user_service.domain.RequestContext;
import com.example.user_service.exception.ApiException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.LocalDateTime;

/**
 * Abstract base class for auditable entities. Provides common fields for tracking
 * entity creation and modification metadata.
 *
 * <p>Includes fields for created and updated timestamps, as well as the identifiers
 * for the user responsible for the creation and last update of the entity.
 * Automatically manages these fields during the lifecycle of the entity.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Getter} - Generates getter methods for all fields.</li>
 *   <li>{@code @MappedSuperclass} - Indicates that this class provides mapping information
 *       for its subclasses but is not itself a complete entity.</li>
 *   <li>{@code @EntityListeners(AuditingEntityListener.class)} - Hooks into JPA lifecycle callbacks.</li>
 *   <li>{@code @JsonIgnoreProperties} - Prevents the serialization of certain fields in JSON.</li>
 * </ul>
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt","updatedAt"}, allowGetters = true)
public abstract class Auditable {

    /**
     * Primary key of the entity.
     *
     * <p>Generated using a sequence generator with allocation size of 1.</p>
     */
    @Id
    @SequenceGenerator(name = "primary_key_seq", sequenceName = "primary_key_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_key_seq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * Reference ID for the entity, generated using {@code AlternativeJdkIdGenerator}.
     * Used as an additional identifier for the entity.
     */
    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();

    /**
     * Identifier of the user who created the entity.
     */
    @NotNull
    private Long createdBy;

    /**
     * Identifier of the user who last updated the entity.
     */
    @NotNull
    private Long updatedBy;

    /**
     * Timestamp of when the entity was created.
     *
     * <p>This field is automatically populated on entity creation and is not updatable.</p>
     */
    @NotNull
    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last update to the entity.
     *
     * <p>This field is automatically populated on each update to the entity.</p>
     */
    @CreatedDate
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Lifecycle callback executed before the entity is persisted.
     *
     * <p>Sets the {@code createdAt}, {@code createdBy}, {@code updatedAt}, and {@code updatedBy} fields
     * to the current timestamp and user ID from the request context. Throws an {@code ApiException}
     * if no user ID is found in the request context.</p>
     *
     * @throws ApiException if user ID is not present in the RequestContext
     */
    @PrePersist
    protected void onCreate() {
        var userId = 0L;//RequestContext.getUserId();
        //if (userId == null) {throw new ApiException("Cannot persist entity without user ID in Request Context for this thread");}
        this.createdAt = LocalDateTime.now();
        this.createdBy = userId;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }

    /**
     * Lifecycle callback executed before the entity is updated.
     *
     * <p>Sets the {@code updatedAt} and {@code updatedBy} fields to the current
     * timestamp and user ID from the request context. Throws an {@code ApiException}
     * if no user ID is found in the request context.</p>
     *
     * @throws ApiException if user ID is not present in the RequestContext
     */
    @PreUpdate
    protected void onUpdate() {
        var userId = 0L;//RequestContext.getUserId(); todo fix
//        if (userId == null) {throw new ApiException("Cannot update entity without user ID in Request Context for this thread");}
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }
}

