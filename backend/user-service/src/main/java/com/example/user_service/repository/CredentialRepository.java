package com.example.user_service.repository;

import com.example.user_service.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing CredentialEntity persistence and query operations.
 *
 * <p>This interface extends {@code JpaRepository}, providing CRUD operations as well as
 * custom query methods for searching credential records by specific fields. It acts as
 * a bridge between the application and the database, allowing for easy access and manipulation
 * of CredentialEntity records.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Repository} - Marks this interface as a Spring Data repository.</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 *     Optional<CredentialEntity> credential = credentialRepository.getCredentialByUserEntityId(1L);
 * </pre>
 */
@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, Long> {

    /**
     * Retrieves a CredentialEntity by the associated UserEntity ID.
     *
     * @param userId the ID of the UserEntity associated with the CredentialEntity
     * @return an Optional containing the CredentialEntity if found, or an empty Optional if not found
     */
    Optional<CredentialEntity> getCredentialByUserEntityId(Long userId);
}
