package com.example.user_service.repository;

import com.example.user_service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing UserEntity persistence and query operations.
 *
 * <p>This interface extends {@code JpaRepository}, providing CRUD operations as well as
 * custom query methods for searching user records by specific fields. It acts as
 * a bridge between the application and the database, allowing for easy access and manipulation
 * of UserEntity records.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Repository} - Marks this interface as a Spring Data repository.</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 *     Optional<UserEntity> user = userRepository.findByEmailIgnoreCase("example@example.com");
 * </pre>
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Finds a UserEntity by its email address, ignoring case.
     *
     * @param email the email address to search for
     * @return an Optional containing the UserEntity if found, or an empty Optional if not found
     */
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    /**
     * Finds a UserEntity by its unique user identifier.
     *
     * @param username the unique user identifier to search for
     * @return an Optional containing the UserEntity if found, or an empty Optional if not found
     */
    Optional<UserEntity> findUserByUserId(String username);
}

