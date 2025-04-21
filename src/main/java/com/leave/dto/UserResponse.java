package com.leave.dto;

import com.leave.shared.enums.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String avatarUrl;
    private UserRole role;
    private String department;
    private String position;
    private String microsoftId;
    private boolean isActive;
    private String createdAt;
    private String updatedAt;
    private Double remainingLeaveDays;
    private String token; // Optional, only if you want to include the token in the response
}
