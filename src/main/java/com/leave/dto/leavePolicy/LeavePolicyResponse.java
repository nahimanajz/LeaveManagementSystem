package com.leave.dto.leavePolicy;

import com.leave.shared.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Leave policy configuration response")
public class LeavePolicyResponse {
    @Schema(description = "Type of leave")
    private LeaveType type;

    @Schema(description = "Default annual balance for this leave type")
    private Double defaultBalance;

    @Schema(description = "Monthly accrual rate")
    private Double monthlyAccrual;

    @Schema(description = "Maximum days that can be carried forward to next year")
    private Double maxCarryForward;

    @Schema(description = "When the policy was created")
    private LocalDateTime createdAt;

    @Schema(description = "When the policy was last updated")
    private LocalDateTime updatedAt;

   
} 