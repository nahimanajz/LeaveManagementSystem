package com.leave.dto.LeaveManagement;

import lombok.Data;

@Data
public class UpdateLeaveBalanceRequest {
        private Long leaveTypeId;
        private double leaveBalance;
}
