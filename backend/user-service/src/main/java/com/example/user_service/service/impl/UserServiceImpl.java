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

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        UserEntity userEntity = userRepository.save(createNewUser(firstName, lastName, email));
        CredentialEntity credentialEntity = new CredentialEntity(userEntity, password);
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new UserEvent(userEntity, EventType.REGISTRATION, Map.of("key", confirmationEntity.getKey())));
    }

    private UserEntity createNewUser(String firstName, String lastName, String email) {
        RoleEntity role = getRoleName(Authority.USER.name());//todo: change to user
        return UserUtils.createUserEntity(firstName, lastName, email, role);
    }

    @Override
    public RoleEntity getRoleName(String name) {
        return roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ApiException("Role not found"));
    }

    @Override
    public void verifyAccountKey(String key) {
        ConfirmationEntity confirmationEntity = getUserConfirmation(key);
        UserEntity userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());;
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    private ConfirmationEntity getUserConfirmation(String key) {
        return confirmationRepository.findByKey(key)
                .orElseThrow(() -> new ApiException("Invalid key"));
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        UserEntity userEntity = getUserEntityByEmail(email);
        RequestContext.setUserId(userEntity.getId());
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(userEntity.getEmail()) == null) {
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
                userCache.put(userEntity.getEmail(), userEntity.getLoginAttempts());
                if (userCache.get(userEntity.getEmail()) > 5) {
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(now());
                userCache.evict(userEntity.getEmail());
            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity getUserEntityByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ApiException("User by email not found"));
    }

    @Override
    public User getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findUserByUserId(userId).orElseThrow(() -> new ApiException("User by user id not found"));
        return UserUtils.fromUserEntity(userEntity, userEntity.getRole(),getUserCredentialById(userEntity.getId()));
    }

    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity = getUserEntityByEmail(email);
        return UserUtils.fromUserEntity(userEntity, userEntity.getRole(),getUserCredentialById(userEntity.getId()));
    }

    @Override
    public CredentialEntity getUserCredentialById(Long userId) {
        return credentialRepository.getCredentialByUserEntityId(userId)
                .orElseThrow(() -> new ApiException("Unable to find user credential"));
    }
}

