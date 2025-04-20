package com.leave.dto.user;

import java.time.LocalDate;

import com.leave.shared.enums.UserRole;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SignupRequest {
    private String email;
    private String name;
    private String position;
    private String department;
    private String avatarUrl;
    private UserRole role;
    private String microsoftId;
    private boolean isActive;
     private LocalDate startDate; 
   
}
