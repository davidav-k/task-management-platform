package com.example.userservice.service;

import com.example.userservice.dto.*;
import com.example.userservice.entity.RefreshToken;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.RoleType;
import com.example.userservice.entity.User;
import com.example.userservice.exception.EmailAlreadyInUseException;
import com.example.userservice.exception.RefreshTokenException;
import com.example.userservice.exception.UsernameAlreadyTakenException;
import com.example.userservice.repo.RoleRepository;
import com.example.userservice.repo.UserRepository;
import com.example.userservice.security.AppUserDetails;
import com.example.userservice.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthRs authenticateUser(LoginRq rq){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                rq.getUsername(),
                rq.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthRs.builder()
                .id(userDetails.getId())
                .token(jwtUtils.generateJwtToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    public void register(@NotNull UserRq rq){

        if (userRepository.existsByUsername(rq.getUsername())) {
            throw new UsernameAlreadyTakenException("Username is already taken");
        }

        if (userRepository.existsByEmail(rq.getEmail())) {
            throw new EmailAlreadyInUseException("Email is already in use");
        }

        Set<Role> roles = rq.getRoles().stream()
                .map(roleName -> roleRepository.findByName(RoleType.valueOf(roleName))
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        User user = User.builder()
                .username(rq.getUsername())
                .email(rq.getEmail())
                .password(passwordEncoder.encode(rq.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);

    }

    public RefreshTokenRs refreshToken (@NotNull RefreshTokenRq rq){
        String rqRefreshToken = rq.getRefreshToken();

        return refreshTokenService.findByRefreshToken(rqRefreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User tokenOwner = userRepository.findById(userId)
                            .orElseThrow(() -> new RefreshTokenException("Exception trying to get token for userId: " + userId));
                    String token = jwtUtils.generateTokenFromUsername(tokenOwner.getUsername());

                    return new RefreshTokenRs(
                            token,
                            refreshTokenService.createRefreshToken(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException(rqRefreshToken, "Refresh token not found"));
    }

    public void logout(){
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails){
            Long userId = userDetails.getId();
            refreshTokenService.deleteByUserId(userId);
        }
    }
}
