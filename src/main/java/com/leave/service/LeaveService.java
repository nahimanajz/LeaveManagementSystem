package com.leave.service;

import com.leave.dto.LeaveRequest;
import com.leave.dto.LeaveResponse;
import com.leave.dto.UserResponse;
import com.leave.dto.leaves.UpdateLeaveRequest;
import com.leave.helpers.NotificationHelper;
import com.leave.model.Leave;
import com.leave.model.LeaveManagement;
import com.leave.model.LeaveType;
import com.leave.model.User;
import com.leave.repository.LeaveManagementRepository;
import com.leave.repository.LeaveRepository;
import com.leave.repository.LeaveTypeRepository;
import com.leave.repository.UserRepository;
import com.leave.shared.enums.LeaveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private LeaveTypeRepository leaveTypeRepo;

    @Autowired
    private LeaveManagementRepository leaveMngmtRepo;

    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public LeaveResponse createLeave(LeaveRequest leaveRequest, MultipartFile document) throws IOException {
        // Fetch the user from the database
        User user = userRepository.findById(leaveRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LeaveType leaveType = leaveTypeRepo.findByName(leaveRequest.getType().trim())
                .orElseThrow(() -> new IllegalArgumentException("Leave type not found"));
       // Verify if this leave is not created in the same year is not created then
        verifyOrCreateLeaveManagement(user, leaveType);

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

        LeaveType leaveType = leaveTypeRepo.findByName(leave.getType())
                .orElseThrow(() -> new IllegalArgumentException("Leave type not found"));

        LeaveManagement leaveManagement = leaveMngmtRepo.findByUserAndLeaveType(leave.getUser(), leaveType)
                .orElseThrow(() -> new IllegalArgumentException("Leave management record not found"));

        leave.setApprover(approver);
        leave.setApprovalStatus(request.getStatus());
        leave.setApproverComment(request.getApproverComment());

        // deduct days given to a user
        checkApprovalStatusAndDeductDays(leave, leaveManagement,request);
        notificationHelper.sendLeaveStatusNotification(leave, approver, request.getStatus().name());
        
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
            userResponse.setLeaveBalances(getUserWithLeaveBalances(leave.getUser()));
            //set position
            // set department
            userResponse.setPosition(leave.getUser().getPosition());
            userResponse.setDepartment(leave.getUser().getDepartment());
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
        // append user responses to userResponse
        return response;
    }

    private void checkApprovalStatusAndDeductDays(Leave leave, LeaveManagement leaveManagement, UpdateLeaveRequest request) {
        // Calculate leave days
        if("APPROVED".equalsIgnoreCase(request.getStatus().toString())){

            long leaveDays = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
            if (!leave.isFullDay()) {
                leaveDays = leaveDays / 2; // Half-day leave
            }
    
            if (leaveManagement.getLeaveBalance() < leaveDays) {
                throw new IllegalArgumentException("Insufficient leave balance");
            }
    
            // Deduct leave days from the balance
            leaveManagement.setLeaveBalance(leaveManagement.getLeaveBalance() - leaveDays);
            leaveMngmtRepo.save(leaveManagement);
        }
    }

    private void verifyOrCreateLeaveManagement(User user, LeaveType leaveType) {
        // Check if LeaveManagement entry exists for the user and leave type
        LeaveManagement leaveManagement = leaveMngmtRepo.findByUserAndLeaveType(user, leaveType)
                .orElseGet(() -> {
                    // Create a new LeaveManagement entry if it doesn't exist
                    LeaveManagement newLeaveManagement = new LeaveManagement();
                    newLeaveManagement.setUser(user);
                    newLeaveManagement.setLeaveType(leaveType);
                    newLeaveManagement.setLeaveBalance(leaveType.getDefaultDays());
                    return leaveMngmtRepo.save(newLeaveManagement);
                });

    }

    private Map<String, Double> getUserWithLeaveBalances(User user) {

        // Fetch leave balances for the user
        List<LeaveManagement> leaveManagements = leaveMngmtRepo.findByUser(user);

        // Map leave balances to a Map<String, Double>
        Map<String, Double> leaveBalances = leaveManagements.stream()
                .collect(Collectors.toMap(
                        lm -> lm.getLeaveType().getName(),
                        LeaveManagement::getLeaveBalance));
        return leaveBalances;
    }
}