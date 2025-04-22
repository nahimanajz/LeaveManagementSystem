package com.leave.dto.user;

import lombok.Data;

@Data
public class VerifyRequest {
    private String email;
    private String microsoftId;
    
}
