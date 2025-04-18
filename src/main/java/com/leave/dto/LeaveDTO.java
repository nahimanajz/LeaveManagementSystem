package com.leave.dto;

import com.leave.shared.enums.LeaveStatus;
import com.leave.shared.enums.LeaveType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LeaveDTO {
    private Long id;
    private Long userId;
    private Long approverId;
    private String approverComment;
    private LeaveType type;
    private String leaveReason;
    private boolean isFullDay;
    private LeaveStatus approvalStatus;
    private String documentUrl;
    private String documentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 