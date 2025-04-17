package com.leave.controller;

import com.leave.dto.SignInRequest;
import com.leave.dto.SignInResponse;
import com.leave.dto.UserResponse;
import com.leave.dto.ErrorResponse;
import com.leave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

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

    @PostMapping("/auth/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            SignInResponse response = userService.signIn(request);
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
} 