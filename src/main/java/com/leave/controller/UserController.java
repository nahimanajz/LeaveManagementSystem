package com.leave.controller;

import com.leave.dto.SignInRequest;
import com.leave.dto.SignInResponse;
import com.leave.dto.UserResponse;
import com.leave.dto.user.SignupRequest;
import com.leave.dto.user.VerifyRequest;
import com.leave.dto.ErrorResponse;
import com.leave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        try {
            List<UserResponse> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching users: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Sign in a user", description = "Authenticate a user and return a token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully signed in"),
            @ApiResponse(responseCode = "400", description = "Invalid request or user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/auth/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            UserResponse response = userService.signIn(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            String errorCode = "INVALID_EMAIL_DOMAIN";
            if (e.getMessage().contains("not found")) {
                errorCode = "USER_NOT_FOUND";
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), errorCode));
        } catch (Exception e) {
            log.error("Error during sign in: ", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR"));
        }
    }

    @Operation(summary = "Sign up a new user", description = "Register a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully signed up"),
            @ApiResponse(responseCode = "400", description = "Invalid request or user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupRequest request) {
        try {
            UserResponse response = userService.signUp(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "BAD_REQUEST"));
        } catch (Exception e) {
            log.error("Error during signup: ", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Internal server error", "SERVER_ERROR"));
        }
    }

    @PostMapping("/find")
    public ResponseEntity<?> getUserByEmailOrMicrosoftId(
            @RequestBody VerifyRequest credentials) {
        
        try {
            UserResponse userResponse = userService.getUserByEmailOrMicrosoftId(credentials.getEmail(),
                    credentials.getMicrosoftId());
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Internal server Error", "SERVER_ERROR"));
        }

    }

}