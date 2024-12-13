package com.example.user_service.service.impl;

import com.example.user_service.entity.ConfirmationEntity;
import com.example.user_service.entity.CredentialEntity;
import com.example.user_service.entity.RoleEntity;
import com.example.user_service.entity.UserEntity;
import com.example.user_service.enumeration.Authority;
import com.example.user_service.enumeration.EventType;
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

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    //    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;


    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        UserEntity userEntity = createNewUser(firstName, lastName, email);
        CredentialEntity credentialEntity = new CredentialEntity(userEntity, password);
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new UserEvent(userEntity, EventType.REGISTRATION, Map.of("key", confirmationEntity.getKey())));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        return roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ApiException("Role not found"));
    }

    private UserEntity createNewUser(String firstName, String lastName, String email) {
        var role = getRoleName(Authority.USER.name());
        return UserUtils.createUserEntity(firstName, lastName, email, role);
    }
}
