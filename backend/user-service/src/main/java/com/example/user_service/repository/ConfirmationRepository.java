package com.example.user_service.repository;

import com.example.user_service.entity.ConfirmationEntity;
import com.example.user_service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing ConfirmationEntity persistence and query operations.
 *
 * <p>This interface extends {@code JpaRepository}, providing CRUD operations as well as
 * custom query methods for searching confirmation records by specific fields. It acts as
 * a bridge between the application and the database, allowing for easy access and manipulation
 * of ConfirmationEntity records.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Repository} - Marks this interface as a Spring Data repository.</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 *     Optional<ConfirmationEntity> confirmation = confirmationRepository.findByKey("sample-key");
 * </pre>
 */
@Repository
public interface ConfirmationRepository extends JpaRepository<ConfirmationEntity, Long> {

    /**
     * Finds a ConfirmationEntity by its unique key.
     *
     * @param key the unique key associated with the ConfirmationEntity
     * @return an Optional containing the ConfirmationEntity if found, or an empty Optional if not found
     */
    Optional<ConfirmationEntity> findByKey(String key);

    /**
     * Finds a ConfirmationEntity associated with a specific UserEntity.
     *
     * @param userEntity the user entity associated with the ConfirmationEntity
     * @return an Optional containing the ConfirmationEntity if found, or an empty Optional if not found
     */
    Optional<ConfirmationEntity> findByUserEntity(UserEntity userEntity);
}


