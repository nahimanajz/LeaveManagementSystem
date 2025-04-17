package com.leave.controller;

import com.leave.dto.LeaveRequest;
import com.leave.dto.LeaveResponse;
import com.leave.dto.ErrorResponse;
import com.leave.dto.LeaveApprovalRequest;
import com.leave.service.LeaveService;
import com.leave.service.FileStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Tag(name = "Leave Management", description = "APIs for managing leave applications")
@RestController
@RequestMapping("/api/leaves")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(summary = "Create a new leave application without document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave application created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeaveResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createLeave(
            @Parameter(description = "User ID", required = true) @RequestParam("userId") Long userId,
            @RequestBody LeaveRequest request) {

        try { 
            LeaveResponse response = leaveService.createLeave(userId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "INVALID_REQUEST"));
        } catch (Exception e) {
            log.error("Error creating leave: ", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR"));
        }
    }

    @Operation(summary = "Create leave application with document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave application with document created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeaveResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "415", description = "Unsupported media type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/create-with-document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocument(
            @Parameter(description = "Leave ID", required = true) @RequestParam("leaveId") Long leaveId,
            @Parameter(description = "Supporting document (PDF, DOC, DOCX, JPG, PNG)", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "document", required = true) MultipartFile document) {

        try {
            // Save file using FileStorageService
            String[] fileDetails = fileStorageService.storeFile(document);
            LeaveResponse leave = leaveService.update(leaveId, fileDetails);

            return ResponseEntity.ok(leave);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "INVALID_REQUEST"));
        } catch (Exception e) {
            log.error("Error creating leave: ", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR"));
        }
    }

    @Operation(summary = "Get user's leave applications", description = "Retrieves all leave applications for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave applications retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeaveResponse>> getLeavesByUser(@PathVariable Long userId) {
        try {
            List<LeaveResponse> leaves = leaveService.getLeavesByUser(userId);
            return ResponseEntity.ok(leaves);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // create method to retrieve all records from leave table
    @Operation(summary = "Get all leave applications", description = "Retrieves all leave applications from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave applications retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<LeaveResponse>> getAllLeaves() {
        try {
            List<LeaveResponse> leaves = leaveService.findAll();
            return ResponseEntity.ok(leaves);
        } catch (Exception e) {
            log.error("Error retrieving all leaves: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveLeave(@PathVariable("id") Long leaveId, @RequestBody LeaveApprovalRequest request) {
        try {
            LeaveResponse leave = leaveService.approveLeave(leaveId, request.getApproverId(), request.getStatus());
            return ResponseEntity.ok(leave);
        } catch (IllegalArgumentException e) {
            log.error("Error approving leave: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "INVALID_REQUEST"));
        } catch (Exception e) {
            log.error("Error approving leave: ", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR"));
        }
    }
}