package com.leave.dto;

import com.leave.shared.enums.UserRole;
import lombok.Data;

@Data
public class SignInResponse {
    private Long id;
    private String email;
    private String displayName;
    private String avatarUrl;
    private UserRole role;
    private TeamResponse team;
    private boolean isActive;
} 