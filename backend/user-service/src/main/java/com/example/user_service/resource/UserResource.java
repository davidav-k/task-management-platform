package com.example.user_service.resource;

import com.example.user_service.domain.Response;
import com.example.user_service.domain.dto.request.UserRequest;
import com.example.user_service.service.UserService;
import com.example.user_service.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static java.util.Collections.emptyMap;

@RestController
@RequestMapping(path = "${api.endpoint.base-url}/user")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        userService.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
        return ResponseEntity.created(getUri()).body(RequestUtils.getResponse(
                request,
                emptyMap(),
                "Account created. Check your email to enable your account",
                HttpStatus.CREATED));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyNewUserAccount(@RequestParam("key") String key, HttpServletRequest request) {
        userService.verifyAccountKey(key);
        return ResponseEntity.ok().body(RequestUtils.getResponse(
                request,
                emptyMap(),
                "Account verified",
                HttpStatus.OK));
    }

//    @PostMapping("/login")
//    public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
//        var authentication = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
//        jwtService.addCookie(response, authentication.getUser(), TokenType.ACCESS);
//        jwtService.addCookie(response, authentication.getUser(), TokenType.REFRESH);
//        return ResponseEntity.ok().body(RequestUtils.getResponse(request, Map.of("user", authentication.getUser()), "Login successful.", HttpStatus.OK));
//    }

    private URI getUri() {
        return URI.create("");
    }

//    private final JwtService jwtService;

//
//    @GetMapping("/profile")
//    public ResponseEntity<Response> getUserProfile(HttpServletRequest request) {
//        var userId = jwtService.extractToken(request, TokenType.ACCESS).map(jwtService::getTokenData).map(data -> data.getUser().getUserId());
//        if (userId.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RequestUtils.getResponse(request, emptyMap(), "Unauthorized access.", HttpStatus.UNAUTHORIZED));
//        }
//        var user = userService.getUserByUserId(userId.get());
//        return ResponseEntity.ok().body(RequestUtils.getResponse(request, Map.of("user", user), "User profile retrieved successfully.", HttpStatus.OK));
//    }
//
//    @PutMapping("/update")
//    public ResponseEntity<Response> updateUser(@RequestBody @Valid UserRequest userRequest, HttpServletRequest request) {
//        var userId = jwtService.extractToken(request, TokenType.ACCESS).map(jwtService::getTokenData).map(data -> data.getUser().getUserId());
//        if (userId.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RequestUtils.getResponse(request, emptyMap(), "Unauthorized access.", HttpStatus.UNAUTHORIZED));
//        }
//        userService.updateUser(userId.get(), userRequest);
//        return ResponseEntity.ok().body(RequestUtils.getResponse(request, emptyMap(), "User updated successfully.", HttpStatus.OK));
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<Response> deleteUser(HttpServletRequest request) {
//        var userId = jwtService.extractToken(request, TokenType.ACCESS).map(jwtService::getTokenData).map(data -> data.getUser().getUserId());
//        if (userId.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RequestUtils.getResponse(request, emptyMap(), "Unauthorized access.", HttpStatus.UNAUTHORIZED));
//        }
//        userService.deleteUser(userId.get());
//        return ResponseEntity.ok().body(RequestUtils.getResponse(request, emptyMap(), "User deleted successfully.", HttpStatus.OK));
//    }
//
//
//    @PostMapping("/refresh")
//    public ResponseEntity<Response> refreshTokens(HttpServletRequest request, HttpServletResponse response) {
//        var refreshToken = jwtService.extractToken(request, TokenType.REFRESH);
//        if (refreshToken.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RequestUtils.getResponse(request, emptyMap(), "Unauthorized access.", HttpStatus.UNAUTHORIZED));
//        }
//        var tokenData = jwtService.getTokenData(refreshToken.get());
//        jwtService.addCookie(response, tokenData.getUser(), TokenType.ACCESS);
//        jwtService.addCookie(response, tokenData.getUser(), TokenType.REFRESH);
//        return ResponseEntity.ok().body(RequestUtils.getResponse(request, emptyMap(), "Tokens refreshed successfully.", HttpStatus.OK));
//    }
}

