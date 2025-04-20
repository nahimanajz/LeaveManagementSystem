package com.leave.dto.leaveType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request object for creating or updating a leave type")
public class LeaveTypeRequest {
    @Schema(description = "Name of the leave type", example = "Sick Leave")
    private String name;

    @Schema(description = "Description of the leave type", example = "Leave for medical purposes")
    private String description;

    @Schema(description = "Color associated with the leave type", example = "#FF5733")
    private String color;

    @Schema(description = "Default number of days for this leave type", example = "20.0")
    private Double defaultDays;

    @Schema(description = "Monthly accrual rate for this leave type", example = "1.66")
    private Double monthlyAccrual;

    @Schema(description = "Maximum number of days that can be carried forward", example = "5.0")
    private Double maxCarryForward;

    @Schema(description = "Whether the leave type is active", example = "true")
    private Boolean isActive;
}