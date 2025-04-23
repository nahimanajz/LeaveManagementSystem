package com.leave.dto.user;

import com.leave.shared.enums.UserRole;
import lombok.Data;

@Data
public class AdminOrManagerRequest {
    private String email;
    private String name;
    private UserRole role;
    private String password;
}
