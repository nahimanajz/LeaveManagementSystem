package com.leave.dto;

import com.leave.shared.enums.LeaveStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LeaveApprovalRequest {
    @Schema(description = "ID of the user approving/rejecting the leave", required = true)
    private Long approverId;
    
    @Schema(description = "New status for the leave request", required = true)
    private LeaveStatus status;
    
    @Schema(description = "Optional comment from the approver", required = false)
    private String comment;
} 