package com.leave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

@Schema(description = "Leave application request details")
@Data
public class LeaveRequest {
    @Schema(description = "User ID", required = true, example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "Type of leave", required = true, example = "ANNUAL")
    private String type;

    @Schema(description = "Reason for leave", example = "Family vacation")
    private String leaveReason;

    @Schema(description = "Whether the leave is for a full day", required = true, example = "true")
    private Boolean isFullDay;

    @Schema(description = "Start date of the leave", required = true, example = "2025-04-20")
    private LocalDate startDate;

    @Schema(description = "End date of the leave", required = true, example = "2025-04-25")
    private LocalDate endDate;

    @Schema(description = "URL to supporting document if any", example = "https://example.com/doc.pdf")
    private String documentUrl;

    @Schema(description = "Name of the supporting document", example = "leave_request.pdf")
    private String documentName;
}