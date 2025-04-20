package com.leave.service;

import com.leave.dto.LeaveRequest;
import com.leave.dto.LeaveResponse;
import com.leave.dto.UserResponse;
import com.leave.dto.leaves.UpdateLeaveRequest;
import com.leave.model.Leave;
import com.leave.model.User;
import com.leave.repository.LeaveRepository;
import com.leave.repository.UserRepository;
import com.leave.shared.enums.LeaveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public LeaveResponse createLeave(LeaveRequest leaveRequest, MultipartFile document) throws IOException {
        // Fetch the user from the database
        User user = userRepository.findById(leaveRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Map LeaveRequest to Leave entity
        Leave leave = new Leave();
        leave.setUser(user);
        leave.setType(leaveRequest.getType());
        leave.setLeaveReason(leaveRequest.getLeaveReason());
        leave.setFullDay(leaveRequest.isFullDay());
        leave.setStartDate(leaveRequest.getStartDate());
        leave.setEndDate(leaveRequest.getEndDate());
        leave.setApprovalStatus(LeaveStatus.PENDING);

        // Handle document upload
        if (document != null && !document.isEmpty()) {
            // Save the document using FileStorageService
            String[] fileDetails = fileStorageService.storeFile(document);
            leave.setDocumentName(fileDetails[0]);
            leave.setDocumentUrl(fileDetails[1]);
        }

        // Save the leave entity to the database
        leave = leaveRepository.save(leave);

        // Map the saved Leave entity to LeaveResponse
        return mapToResponse(leave);
    }

    @Transactional
    public List<LeaveResponse> getLeavesByUserId(Long userId) {
        List<Leave> leaves = leaveRepository.findByUserId(userId);
        return leaves.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public List<LeaveResponse> getAllLeaves() {
        List<Leave> leaves = leaveRepository.findAll();
        return leaves.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public LeaveResponse updateLeave(Long id, UpdateLeaveRequest request) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave not found"));

        User approver = userRepository.findById(request.getApproverId())
                .orElseThrow(() -> new IllegalArgumentException("Approver not found"));

        leave.setApprover(approver);
        leave.setApprovalStatus(request.getStatus());
        leave.setApproverComment(request.getApproverComment());
        leave = leaveRepository.save(leave);

        return mapToResponse(leave);
    }

    private LeaveResponse mapToResponse(Leave leave) {
        LeaveResponse response = new LeaveResponse();
        response.setId(leave.getId());
        response.setType(leave.getType());
        response.setLeaveReason(leave.getLeaveReason());
        response.setFullDay(leave.isFullDay());
        response.setStartDate(leave.getStartDate());
        response.setEndDate(leave.getEndDate());
        response.setApprovalStatus(leave.getApprovalStatus());
        response.setDocumentName(leave.getDocumentName());
        response.setDocumentUrl(leave.getDocumentUrl());
        response.setCreatedAt(leave.getCreatedAt());
        response.setApproverComment(leave.getApproverComment());
        response.setUpdatedAt(leave.getUpdatedAt());

        // Map user information
        if (leave.getUser() != null) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(leave.getUser().getId());
            userResponse.setName(leave.getUser().getName());
            userResponse.setEmail(leave.getUser().getEmail());
            userResponse.setMicrosoftId(leave.getUser().getMicrosoftId());
            response.setUser(userResponse);
        }

        // Map approver information (if available)
        if (leave.getApprover() != null) {
            UserResponse approverResponse = new UserResponse();
            approverResponse.setId(leave.getUser().getId());
            approverResponse.setName(leave.getUser().getName());
            approverResponse.setEmail(leave.getUser().getEmail());
            approverResponse.setMicrosoftId(leave.getUser().getMicrosoftId());
            response.setApprover(approverResponse);
        }
        return response;
    }
}