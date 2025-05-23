package com.leave.dto;

import com.leave.shared.enums.UserRole;
import lombok.Data;

@Data
public class SignInResponse {
    private Long id;
    private String email;
    private String name;
    private String position;
    private String department;
    private String avatarUrl;
    private UserRole role;
    private String microsoftId;
    private boolean isActive;
} 