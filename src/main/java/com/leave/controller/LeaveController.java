package com.leave.controller;

import com.leave.dto.ErrorResponse;
import com.leave.dto.LeaveRequest;
import com.leave.dto.LeaveResponse;
import com.leave.dto.leaves.UpdateLeaveRequest;
import com.leave.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Operation(summary = "Create a new leave application")
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<?> createLeave(
            @RequestPart("leaveRequest") @Valid LeaveRequest leaveRequest,
            @RequestPart(value = "document", required = false) MultipartFile document) {
        
        try {
            LeaveResponse leaveResponse = leaveService.createLeave(leaveRequest, document);
            return ResponseEntity.ok(leaveResponse);
        
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ErrorResponse(e.getMessage(), "ERROR-CREATING-LEAVE")); // Handle the exception appropriately
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeaveResponse>> getLeavesByUserId(@PathVariable Long userId) {
        List<LeaveResponse> leaves = leaveService.getLeavesByUserId(userId);
        return ResponseEntity.ok(leaves);
    }

    @GetMapping
    public ResponseEntity<List<LeaveResponse>> getAllLeaves() {
        List<LeaveResponse> leaves = leaveService.getAllLeaves();
        return ResponseEntity.ok(leaves);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveResponse> updateLeave(
            @PathVariable Long id,
            @RequestBody UpdateLeaveRequest request) {
        LeaveResponse updatedLeave = leaveService.updateLeave(id, request);
        return ResponseEntity.ok(updatedLeave);
    }
}