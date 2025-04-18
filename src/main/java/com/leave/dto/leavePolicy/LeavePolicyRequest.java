package com.leave.dto.leavePolicy;

import com.leave.shared.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Leave policy configuration request")
public class LeavePolicyRequest {
    @Schema(description = "Type of leave")
    private LeaveType type;

    @Schema(description = "Default annual balance for this leave type")
    private Double defaultBalance;

    @Schema(description = "Monthly accrual rate")
    private Double monthlyAccrual;

    @Schema(description = "Maximum days that can be carried forward to next year")
    private Double maxCarryForward;
}
