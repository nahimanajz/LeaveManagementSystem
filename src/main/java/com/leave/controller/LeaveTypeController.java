package com.leave.controller;

import com.leave.dto.leaveType.LeaveTypeRequest;
import com.leave.dto.leaveType.LeaveTypeResponse;
import com.leave.service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-types")
public class LeaveTypeController {

    @Autowired
    private LeaveTypeService leaveTypeService;

    // Get all leave types
    @GetMapping
    public ResponseEntity<List<LeaveTypeResponse>> getAllLeaveTypes() {
        List<LeaveTypeResponse> leaveTypes = leaveTypeService.getAllLeaveTypes();
        return ResponseEntity.ok(leaveTypes);
    }

    // Get a specific leave type by ID
    @GetMapping("/{id}")
    public ResponseEntity<LeaveTypeResponse> getLeaveTypeById(@PathVariable Long id) {
        LeaveTypeResponse leaveType = leaveTypeService.getLeaveTypeById(id);
        return ResponseEntity.ok(leaveType);
    }

    // Create a new leave type
    @PostMapping
    public ResponseEntity<LeaveTypeResponse> createLeaveType(@RequestBody LeaveTypeRequest request) {
        LeaveTypeResponse leaveType = leaveTypeService.createLeaveType(request);
        return ResponseEntity.ok(leaveType);
    }

    // Update an existing leave type
    @PutMapping("/{id}")
    public ResponseEntity<LeaveTypeResponse> updateLeaveType(
            @PathVariable Long id,
            @RequestBody LeaveTypeRequest request
    ) {
        LeaveTypeResponse leaveType = leaveTypeService.updateLeaveType(id, request);
        return ResponseEntity.ok(leaveType);
    }

    // Delete a leave type
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveType(@PathVariable Long id) {
        leaveTypeService.deleteLeaveType(id);
        return ResponseEntity.noContent().build();
    }
}