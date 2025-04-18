package com.leave.controller;

import com.leave.dto.leavePolicy.LeavePolicyRequest;
import com.leave.dto.leavePolicy.LeavePolicyResponse;
import com.leave.dto.ErrorResponse;
import com.leave.service.LeavePolicyService;
import com.leave.shared.enums.LeaveType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Tag(name = "Leave Policy Management", description = "APIs for managing leave policies")
@RestController
@RequestMapping("/api/leave-policies")
public class LeavePolicyController {
    
    @Autowired
    private LeavePolicyService leavePolicyService;

    @Operation(summary = "Create a new leave policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy created successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeavePolicyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid policy configuration"),
            @ApiResponse(responseCode = "409", description = "Policy already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    
    @PostMapping
    public ResponseEntity<?> createPolicy(@RequestBody LeavePolicyRequest request) {
        try {
            LeavePolicyResponse response = leavePolicyService.addLeavePolicy(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error creating leave policy: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "INVALID_POLICY"));
        } catch (Exception e) {
            log.error("Error creating leave policy: ", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR"));
        }
    }

    @Operation(summary = "Update an existing leave policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy updated successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeavePolicyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid policy configuration"),
            @ApiResponse(responseCode = "404", description = "Policy not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{type}")
    public ResponseEntity<?> updatePolicy(
            @Parameter(description = "Type of leave", required = true)
            @PathVariable LeaveType type,
            @RequestBody LeavePolicyRequest request) {
        try {
            LeavePolicyResponse response = leavePolicyService.updateLeavePolicy(type, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error updating leave policy: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "INVALID_POLICY"));
        } catch (Exception e) {
            log.error("Error updating leave policy: ", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR"));
        }
    }

    @Operation(summary = "Get a specific leave policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy retrieved successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeavePolicyResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{type}")
    public ResponseEntity<?> getPolicy(
            @Parameter(description = "Type of leave", required = true)
            @PathVariable LeaveType type) {
        try {
            List<LeavePolicyResponse> response = leavePolicyService.getLeavePolicy(type);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving leave policy: ", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR"));
        }
    }

    @Operation(summary = "Get all leave policies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policies retrieved successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeavePolicyResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<?> getAllPolicies() {
        try {
            List<LeavePolicyResponse> responses = leavePolicyService.getAllLeavePolicies();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error retrieving leave policies: ", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR"));
        }
    }
}