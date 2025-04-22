package com.leave.dto;


import java.util.Map;

import com.leave.dto.LeaveManagement.LeaveManagementReponse;
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
    private String token; 
    private Map<String, Double> leaveBalances;
}
