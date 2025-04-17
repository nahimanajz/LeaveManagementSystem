package com.leave.dto;

import com.leave.shared.enums.LeaveStatus;
import lombok.Data;

@Data
public class LeaveApprovalRequest {
    private Long approverId;
    private LeaveStatus status;
} 