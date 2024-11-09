package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AuthRs {

    private Long id;
    private String token;
    private String refreshToken;
    private String username;
    private String email;
    private List<String> roles;
}
