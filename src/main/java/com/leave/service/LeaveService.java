package com.leave.service;

import com.leave.dto.LeaveRequest;
import com.leave.dto.LeaveResponse;
import com.leave.helpers.NotificationHelper;
import com.leave.model.Leave;
import com.leave.model.User;
import com.leave.repository.LeaveRepository;
import com.leave.repository.UserRepository;
import com.leave.shared.enums.LeaveStatus;
import com.leave.shared.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {
    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationHelper notificationHelper;

    @Transactional
    public LeaveResponse createLeave(Long userId, LeaveRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Leave leave = new Leave();
        leave.setUser(user);
        leave.setType(request.getType());
        leave.setLeaveReason(request.getLeaveReason());
        leave.setFullDay(request.isFullDay());
        leave.setApprovalStatus(LeaveStatus.PENDING);

        leave = leaveRepository.save(leave);
        return mapToResponse(leave);
    }

    public List<LeaveResponse> getLeavesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return leaveRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<LeaveResponse> findAll() {
        List<Leave> leaves = leaveRepository.findAll();
        return leaves.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LeaveResponse findById(Long leaveId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave not found"));
        return mapToResponse(leave);
    }

    @Transactional
    public LeaveResponse approveLeave(Long leaveId, Long approverId, LeaveStatus status, String comment) {
        // Get the leave request
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        // Get the approver
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new IllegalArgumentException("Approver not found"));

        // Verify if approver is a manager or admin
        if (approver.getRole() != UserRole.MANAGER && approver.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only managers and admins can approve/reject leave requests");
        }

        // Update leave status
        leave.setApprovalStatus(status);
        leave.setApprover(approver);
        leave.setApproverComment(comment); 
        
        leave = leaveRepository.save(leave);
        
        // Send notifications
        notificationHelper.sendLeaveStatusNotification(leave, approver, status);
        
        return mapToResponse(leave);
    }

    public LeaveResponse update(Long leaveId, String[] fileDetails) {

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave not found"));

        leave.setDocumentUrl(fileDetails[1]);
        // Save updated leave
        leave = leaveRepository.save(leave);
        return mapToResponse(leave);

    }

    private LeaveResponse mapToResponse(Leave leave) {
        LeaveResponse response = new LeaveResponse();
        response.setId(leave.getId());
        response.setUser(userService.mapToUserResponse(leave.getUser()));
        response.setApprover(leave.getApprover() != null ? userService.mapToUserResponse(leave.getApprover()) : null);
        response.setApproverComment(leave.getApproverComment());
        response.setType(leave.getType());
        response.setLeaveReason(leave.getLeaveReason());
        response.setFullDay(leave.isFullDay());
        response.setApprovalStatus(leave.getApprovalStatus());
        response.setDocumentUrl(leave.getDocumentUrl());
        response.setDocumentName(leave.getDocumentName());
        response.setCreatedAt(leave.getCreatedAt());
        response.setUpdatedAt(leave.getUpdatedAt());
        return response;
    }

}