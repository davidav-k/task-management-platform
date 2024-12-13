package com.example.user_service.service;

import com.example.user_service.entity.RoleEntity;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName(String name);
}
