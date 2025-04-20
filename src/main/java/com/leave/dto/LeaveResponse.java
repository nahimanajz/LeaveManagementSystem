package com.leave.dto;

import com.leave.shared.enums.LeaveStatus;
import com.leave.shared.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "Leave application response details")
@Data
public class LeaveResponse {
    @Schema(description = "Leave application ID", example = "1")
    private Long id;

    @Schema(description = "User who applied for leave")
    private UserResponse user;

    @Schema(description = "User who approved/rejected the leave")
    private UserResponse approver;

    @Schema(description = "Comment from the user who approved/rejected the leave")
    private String approverComment;

    @Schema(description = "Type of leave", example = "ANNUAL")
    private String type;

    @Schema(description = "Reason for leave", example = "Family vacation")
    private String leaveReason;

    @Schema(description = "Is the leave a full day", example = "true")
    private boolean isFullDay;

    @Schema(description = "Current status of the leave application", example = "PENDING")
    private LeaveStatus approvalStatus;

    @Schema(description = "URL to supporting document if any", example = "https://example.com/doc.pdf")
    private String documentUrl;

    @Schema(description = "Name of the supporting document if any")
    private String documentName;

    @Schema(description = "When the leave application was created")
    private LocalDateTime createdAt;

    @Schema(description = "When the leave application was last updated")
    private LocalDateTime updatedAt;

    @Schema(description = "User ID of the user who applied for leave")
    private Long userId;

    @Schema(description = "Start date of the leave")
    private LocalDate startDate;

    @Schema(description = "End date of the leave")
    private LocalDate endDate;

    @Schema(description = "ID of the user who approved/rejected the leave")
    private Long approverId;
} 