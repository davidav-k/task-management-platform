package com.example.userservice.service;

import com.example.userservice.dto.UserRs;
import com.example.userservice.dto.UserToUserRsConverter;
import com.example.userservice.entity.User;
import com.example.userservice.security.AppUserPrincipal;
import com.example.userservice.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;

    private final UserToUserRsConverter userToUserRsConverter;

    public Map<String, Object> createLoginInfo(@NotNull Authentication authentication) {

        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        UserRs userRs = userToUserRsConverter.convert(user);

        String token = jwtProvider.createToken(authentication);

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("userInfo", userRs);
        loginInfo.put("token", token);

        return loginInfo;
    }

}
