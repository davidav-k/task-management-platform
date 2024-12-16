package com.example.user_service.repository;

import com.example.user_service.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing RoleEntity persistence and query operations.
 *
 * <p>This interface extends {@code JpaRepository}, providing CRUD operations as well as
 * custom query methods for searching role records by specific fields. It acts as
 * a bridge between the application and the database, allowing for easy access and manipulation
 * of RoleEntity records.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Repository} - Marks this interface as a Spring Data repository.</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 *     Optional<RoleEntity> role = roleRepository.findByNameIgnoreCase("ADMIN");
 * </pre>
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Finds a RoleEntity by its name, ignoring case.
     *
     * @param name the name of the role to search for
     * @return an Optional containing the RoleEntity if found, or an empty Optional if not found
     */
    Optional<RoleEntity> findByNameIgnoreCase(String name);
}
