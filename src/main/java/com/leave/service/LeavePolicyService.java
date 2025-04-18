package com.leave.service;

import com.leave.dto.leavePolicy.LeavePolicyRequest;
import com.leave.dto.leavePolicy.LeavePolicyResponse;
import com.leave.model.LeavePolicy;
import com.leave.repository.LeavePolicyRepository;
import com.leave.shared.enums.LeaveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeavePolicyService {
    @Autowired
    private LeavePolicyRepository leavePolicyRepository;

    @Transactional
    public LeavePolicyResponse addLeavePolicy(LeavePolicyRequest request) {
        // Validate request
        validatePolicyRequest(request, true);

        // Check if policy already exists
        if (leavePolicyRepository.findByType(request.getType()).isPresent()) {
            throw new IllegalArgumentException("Policy for leave type " + request.getType() + " already exists");
        }

        // Create new policy
        LeavePolicy policy = new LeavePolicy();
        policy.setType(request.getType());
        policy.setDefaultBalance(request.getDefaultBalance());
        policy.setMonthlyAccrual(request.getMonthlyAccrual());
        policy.setMaxCarryForward(request.getMaxCarryForward());

        // Save policy
        policy = leavePolicyRepository.save(policy);

        // Convert to response
        return mapToResponse(policy);
    }

    @Transactional
    public LeavePolicyResponse updateLeavePolicy(LeaveType type, LeavePolicyRequest request) {
        // Validate request
        validatePolicyRequest(request, false);

        // Get existing policy
        LeavePolicy policy = leavePolicyRepository.findByType(type)
                .orElseThrow(() -> new IllegalArgumentException("Policy for leave type " + type + " not found"));

        // Update only provided fields
        if (request.getDefaultBalance() != null) {
            policy.setDefaultBalance(request.getDefaultBalance());
        }
        if (request.getMonthlyAccrual() != null) {
            policy.setMonthlyAccrual(request.getMonthlyAccrual());
        }
        if (request.getMaxCarryForward() != null) {
            policy.setMaxCarryForward(request.getMaxCarryForward());
        }

        // Save updated policy
        policy = leavePolicyRepository.save(policy);

        // Convert to response
        return mapToResponse(policy);
    }

    @Transactional(readOnly = true)
    public List<LeavePolicyResponse> getLeavePolicy(LeaveType type) {
        return leavePolicyRepository.findByType(type)
                .map(policy -> {
                    List<LeavePolicyResponse> response = new ArrayList<>();
                    response.add(mapToResponse(policy));
                    return response;
                })
                .orElse(new ArrayList<>());
    }

    @Transactional(readOnly = true)
    public List<LeavePolicyResponse> getAllLeavePolicies() {
        return leavePolicyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void validatePolicyRequest(LeavePolicyRequest request, boolean isCreate) {
        if (isCreate) {
            if (request.getType() == null) {
                throw new IllegalArgumentException("Leave type is required");
            }
            if (request.getDefaultBalance() == null) {
                throw new IllegalArgumentException("Default balance is required");
            }
            if (request.getMonthlyAccrual() == null) {
                throw new IllegalArgumentException("Monthly accrual is required");
            }
            if (request.getMaxCarryForward() == null) {
                throw new IllegalArgumentException("Maximum carry forward is required");
            }
        }

        if (request.getDefaultBalance() != null && request.getDefaultBalance() < 0) {
            throw new IllegalArgumentException("Default balance cannot be negative");
        }
        if (request.getMonthlyAccrual() != null && request.getMonthlyAccrual() < 0) {
            throw new IllegalArgumentException("Monthly accrual cannot be negative");
        }
        if (request.getMaxCarryForward() != null && request.getMaxCarryForward() < 0) {
            throw new IllegalArgumentException("Maximum carry forward cannot be negative");
        }
    }

    private LeavePolicyResponse mapToResponse(LeavePolicy policy) {
        LeavePolicyResponse response = new LeavePolicyResponse();
        response.setType(policy.getType());
        response.setDefaultBalance(policy.getDefaultBalance());
        response.setMonthlyAccrual(policy.getMonthlyAccrual());
        response.setMaxCarryForward(policy.getMaxCarryForward());
        response.setCreatedAt(policy.getCreatedAt());
        response.setUpdatedAt(policy.getUpdatedAt());
        return response;
    }
}
