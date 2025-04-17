package com.leave.dto;

import com.leave.shared.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Leave application request details")
@Data
public class LeaveRequest {
    @Schema(description = "Type of leave", required = true, example = "ANNUAL")
    private LeaveType type;

    @Schema(description = "Reason for leave", example = "Family vacation")
    private String leaveReason;

    @Schema(description = "Whether the leave is for a full day", required = true, example = "true")
    private boolean isFullDay;

    @Schema(description = "URL to supporting document if any", example = "https://example.com/doc.pdf")
    private String documentUrl;
    
} 