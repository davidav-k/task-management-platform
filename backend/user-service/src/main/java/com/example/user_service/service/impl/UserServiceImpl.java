package com.example.user_service.service.impl;

import com.example.user_service.cache.CacheStore;
import com.example.user_service.domain.RequestContext;
import com.example.user_service.dto.User;
import com.example.user_service.entity.ConfirmationEntity;
import com.example.user_service.entity.CredentialEntity;
import com.example.user_service.entity.RoleEntity;
import com.example.user_service.entity.UserEntity;
import com.example.user_service.enumeration.Authority;
import com.example.user_service.enumeration.EventType;
import com.example.user_service.enumeration.LoginType;
import com.example.user_service.event.UserEvent;
import com.example.user_service.exception.ApiException;
import com.example.user_service.repository.ConfirmationRepository;
import com.example.user_service.repository.CredentialRepository;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import com.example.user_service.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


import java.util.Map;

import static java.time.LocalDate.now;

/**
 * Implementation of the UserService interface for handling user-related business logic.
 *
 * <p>This class provides methods to create users, retrieve roles, and handle user-related
 * operations. It works with multiple repositories, including UserRepository, RoleRepository,
 * CredentialRepository, and ConfirmationRepository, to persist and manage user data.
 * Events are published using the ApplicationEventPublisher to notify other parts of
 * the application of user-related events.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Service} - Marks this class as a Spring service component, enabling dependency injection.</li>
 *   <li>{@code @Transactional(rollbackOn = Exception.class)} - Ensures that database operations are transactional and rolls back on any exception.</li>
 *   <li>{@code @RequiredArgsConstructor} - Generates a constructor with required arguments for all final fields.</li>
 *   <li>{@code @Slf4j} - Provides a logger for logging error messages and other important information.</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 *     userService.createUser("John", "Doe", "john.doe@example.com", "password123");
 *     RoleEntity role = userService.getRoleName("ADMIN");
 * </pre>
 */
@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final ApplicationEventPublisher publisher;
    private final CacheStore<String, Integer> userCache;

    /**
     * Creates a new user with the specified details.
     *
     * <p>This method creates a new UserEntity and its associated CredentialEntity and
     * ConfirmationEntity. It also publishes a UserEvent to notify other components
     * that a new user has been registered.</p>
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email address of the user
     * @param password  the password for the user account
     */
    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        UserEntity userEntity = createNewUser(firstName, lastName, email);
        CredentialEntity credentialEntity = new CredentialEntity(userEntity, password);
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new UserEvent(userEntity, EventType.REGISTRATION, Map.of("key", confirmationEntity.getKey())));
    }

    /**
     * Retrieves a RoleEntity by its name.
     *
     * <p>This method finds and returns a RoleEntity that matches the provided role name.
     * If no role is found, an ApiException is thrown.</p>
     *
     * @param name the name of the role to search for
     * @return the RoleEntity corresponding to the specified role name
     * @throws ApiException if the role is not found
     */
    @Override
    public RoleEntity getRoleName(String name) {
        return roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ApiException("Role not found"));
    }

    @Override
    public void verifyNewUserAccount(String key) {
        ConfirmationEntity confirmationEntity = confirmationRepository.findByKey(key)
                .orElseThrow(() -> new ApiException("Invalid key"));
        UserEntity userEntity = userRepository.findByEmailIgnoreCase(confirmationEntity.getUserEntity().getEmail())
                .orElseThrow(() -> new ApiException("User not found"));
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        UserEntity userByEmail = getUserByEmail(email);
        RequestContext.setUserId(userByEmail.getId());
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(userByEmail.getEmail()) == null) {
                    userByEmail.setLoginAttempts(0);
                    userByEmail.setAccountNonLocked(true);
                }
                userByEmail.setLoginAttempts(userByEmail.getLoginAttempts() + 1);
                userCache.put(userByEmail.getEmail(), userByEmail.getLoginAttempts());
                if (userCache.get(userByEmail.getEmail()) > 5) {
                    userByEmail.setAccountNonLocked(false);

                }
            }
            case LOGIN_SUCCESS -> {
                userByEmail.setAccountNonLocked(true);
                userByEmail.setLoginAttempts(0);
                userByEmail.setLastLogin(now());
                userCache.evict(userByEmail.getEmail());
            }

        }
        userRepository.save(userByEmail);

    }

    private UserEntity getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ApiException("User not found"));
    }

    public User getUserByUserId(String userId) {return null;}

    /**
     * Creates a new UserEntity with the specified details.
     *
     * <p>This method retrieves the default "USER" role and creates a new UserEntity
     * using the provided first name, last name, and email. The UserEntity is not
     * saved in the database by this method.</p>
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email address of the user
     * @return the newly created UserEntity
     */
    private UserEntity createNewUser(String firstName, String lastName, String email) {
        var role = getRoleName(Authority.USER.name());
        return UserUtils.createUserEntity(firstName, lastName, email, role);
    }
}

