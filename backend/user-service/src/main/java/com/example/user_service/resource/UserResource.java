package com.example.user_service.resource;

import com.example.user_service.domain.Response;
import com.example.user_service.domain.dto.request.UserRequest;
import com.example.user_service.service.UserService;
import com.example.user_service.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static java.util.Collections.emptyMap;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        userService.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
        return ResponseEntity.created(getUri()).body(RequestUtils.getResponse(request, emptyMap(), "Account created. Check your email to enable your account", HttpStatus.CREATED));
    }

    private URI getUri() {
        return URI.create("");
    }
}
