package com.example.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class UserRs {

    Long id;
    String username;
    String email;
    List<String> roles;
    boolean isEnabled;

    }
