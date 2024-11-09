package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRs {

    private Long id;

    private String username;

    private String email;

    private List<String> roles;

    private boolean isEnabled;

    }
