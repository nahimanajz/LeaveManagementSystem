package com.leave.dto.user;

import com.leave.shared.enums.UserRole;

import lombok.Data;

@Data
public class AdminOrManagerResponse {
    private Long id;
    private String email;
    private UserRole role;
    private String name;
    
}
