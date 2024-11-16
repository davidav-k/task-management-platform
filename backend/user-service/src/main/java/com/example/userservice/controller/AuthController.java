package com.example.userservice.controller;

import com.example.userservice.dto.Result;
import com.example.userservice.dto.StatusCode;
import com.example.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("${api.endpoint.base-url}/user")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result getLoginInfo(Authentication authentication){

      log.debug("Authenticated user: {}", authentication.getName());

        Map<String, Object> loginInfo = authService.createLoginInfo(authentication);

        return new Result(true, StatusCode.SUCCESS, "User Info ane JSON Web Token", loginInfo);
    }

}
