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
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static java.util.Collections.emptyMap;

/**
 * Controller class for managing user-related HTTP requests.
 *
 * <p>This class serves as a REST controller for handling requests related to user operations.
 * It provides an endpoint for user registration and interacts with the {@code UserService}
 * to process incoming user data. The response is returned as a standardized ResponseEntity
 * with status and message details.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @RestController} - Marks this class as a REST controller, allowing it to handle HTTP requests.</li>
 *   <li>{@code @RequestMapping(path = "/user")} - Maps all incoming requests with the path "/user" to this controller.</li>
 *   <li>{@code @RequiredArgsConstructor} - Generates a constructor with required arguments for all final fields.</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 *     POST /user/register
 *     Request Body: {
 *         "firstName": "John",
 *         "lastName": "Doe",
 *         "email": "john.doe@example.com",
 *         "password": "password123"
 *     }
 * </pre>
 */
@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {

    /**
     * Service that handles user-related business logic.
     */
    private final UserService userService;

    /**
     * Handles HTTP POST requests to register a new user.
     *
     * <p>This method takes user registration details from the request body and calls
     * the {@code UserService} to create a new user. It returns a ResponseEntity with a
     * message indicating the account creation status.</p>
     *
     * @param user the user request payload containing user details for registration
     * @param request the HttpServletRequest object representing the incoming HTTP request
     * @return a ResponseEntity containing the response message and HTTP status code
     */
    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        userService.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
        return ResponseEntity.created(getUri()).body(RequestUtils.getResponse(request, emptyMap(), "Account created. Check your email to enable your account", HttpStatus.CREATED));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyNewUserAccount(@RequestParam("key") String key, HttpServletRequest request) {
        userService.verifyNewUserAccount(key);
        return ResponseEntity.ok().body(RequestUtils.getResponse(request, emptyMap(), "Account verified", HttpStatus.OK));
    }

    /**
     * Generates a URI for the created resource.
     *
     * <p>This method returns a URI pointing to the location of the newly created user.
     * Currently, it returns an empty URI, but it can be customized to return a specific
     * location.</p>
     *
     * @return a URI pointing to the location of the created user resource
     */
    private URI getUri() {
        return URI.create("");
    }
}

