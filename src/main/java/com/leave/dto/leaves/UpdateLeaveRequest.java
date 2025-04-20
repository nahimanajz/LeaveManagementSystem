package com.leave.dto.leaves;

import com.leave.shared.enums.LeaveStatus;
import lombok.Data;

@Data
public class UpdateLeaveRequest {
    private Long approverId;
    private LeaveStatus status;
    private String approverComment;
}