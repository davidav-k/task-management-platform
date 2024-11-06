package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.repo.UserRepository;
import com.example.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("signin")
    public Result authenticateUser(@RequestBody @Valid LoginRq rq){
        AuthRs authRs = authService.authenticateUser(rq);
        return new Result(true, StatusCode.SUCCESS, "User login successfully", authRs);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserRq rq) {
        authService.register(rq);
        return new Result(true,StatusCode.SUCCESS, "User registered");
    }

    @PostMapping("/refresh-token")
    public Result refreshToken(@RequestBody RefreshTokenRq rq) {
        RefreshTokenRs rs = authService.refreshToken(rq);
        return new Result(true,StatusCode.SUCCESS, "Refresh token returned", rs);
    }

    @PostMapping("/logout")
    public Result logout(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logout();
        return new Result(true,StatusCode.SUCCESS, "User logout. User name is: " + userDetails.getUsername());
    }


}
