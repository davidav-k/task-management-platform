package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("$.{api.endpoint.base-url}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        UserRs rs = authService.register(request);
        return new Result(true, StatusCode.SUCCESS, "User registered successfully", rs);
    }

    @PostMapping("/login")
    public Result login(@RequestBody @Valid UserLoginRequest rq){
        String token = authService.login(rq);
        return new Result(true, 200, "User login successfully", token);

    }
}
