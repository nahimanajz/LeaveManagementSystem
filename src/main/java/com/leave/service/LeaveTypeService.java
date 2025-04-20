package com.leave.service;

import com.leave.dto.leaveType.LeaveTypeRequest;
import com.leave.dto.leaveType.LeaveTypeResponse;
import com.leave.model.LeaveType;
import com.leave.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    public List<LeaveTypeResponse> getAllLeaveTypes() {
        return leaveTypeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public LeaveTypeResponse getLeaveTypeById(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave type not found"));
        return mapToResponse(leaveType);
    }

    public LeaveTypeResponse createLeaveType(LeaveTypeRequest request) {
        LeaveType leaveType = mapToEntity(request);
        leaveType = leaveTypeRepository.save(leaveType);
        return mapToResponse(leaveType);
    }

    public LeaveTypeResponse updateLeaveType(Long id, LeaveTypeRequest request) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave type not found"));

        leaveType.setName(request.getName());
        leaveType.setDescription(request.getDescription());
        leaveType.setColor(request.getColor());
        leaveType.setDefaultDays(request.getDefaultDays());
        leaveType.setMonthlyAccrual(request.getMonthlyAccrual());
        leaveType.setMaxCarryForward(request.getMaxCarryForward());
        leaveType.setActive(request.getIsActive());

        leaveType = leaveTypeRepository.save(leaveType);
        return mapToResponse(leaveType);
    }

    public void deleteLeaveType(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave type not found"));
        leaveTypeRepository.delete(leaveType);
    }

    private LeaveType mapToEntity(LeaveTypeRequest request) {
        LeaveType leaveType = new LeaveType();
        leaveType.setName(request.getName());
        leaveType.setDescription(request.getDescription());
        leaveType.setColor(request.getColor());
        leaveType.setDefaultDays(request.getDefaultDays());
        leaveType.setMonthlyAccrual(request.getMonthlyAccrual());
        leaveType.setMaxCarryForward(request.getMaxCarryForward());
        leaveType.setActive(request.getIsActive());
        return leaveType;
    }

    private LeaveTypeResponse mapToResponse(LeaveType leaveType) {
        LeaveTypeResponse response = new LeaveTypeResponse();
        response.setId(leaveType.getId());
        response.setName(leaveType.getName());
        response.setDescription(leaveType.getDescription());
        response.setColor(leaveType.getColor());
        response.setDefaultDays(leaveType.getDefaultDays());
        response.setMonthlyAccrual(leaveType.getMonthlyAccrual());
        response.setMaxCarryForward(leaveType.getMaxCarryForward());
        response.setIsActive(leaveType.isActive());
        response.setCreatedAt(leaveType.getCreatedAt().toString());
        response.setUpdatedAt(leaveType.getUpdatedAt().toString());
        return response;
    }
}