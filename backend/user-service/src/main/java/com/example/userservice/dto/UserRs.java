package com.example.userservice.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserRs {

    Long id;
    String username;
    String email;
    String roles;
    boolean isEnabled;

    }
