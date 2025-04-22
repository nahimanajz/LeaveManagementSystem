package com.leave.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.dto.LeaveManagement.LeaveManagementReponse;
import com.leave.model.LeaveManagement;
import com.leave.model.LeaveType;
import com.leave.model.User;
import com.leave.repository.LeaveManagementRepository;
import com.leave.repository.LeaveTypeRepository;
import com.leave.repository.UserRepository;

@Service
public class LeaveAccrualService {

    private final LeaveManagementRepository leaveManagementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepo;

    @Autowired
    private LeaveManagementRepository leaveMngtRepo;

    LeaveAccrualService(LeaveManagementRepository leaveManagementRepository) {
        this.leaveManagementRepository = leaveManagementRepository;
    }

    @Transactional
    public void autoAccrueLeave() {

        List<LeaveType> leaveTypes = leaveTypeRepo.findAll();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            for (LeaveType leaveType : leaveTypes) {
                LeaveManagement leaveManagement = leaveMngtRepo
                        .findByUserAndLeaveType(user, leaveType)
                        .orElseGet(() -> {
                            LeaveManagement newLeaveManagement = new LeaveManagement();
                            newLeaveManagement.setUser(user);
                            newLeaveManagement.setLeaveType(leaveType);
                            newLeaveManagement.setLeaveBalance(0.0); 
                            return leaveManagementRepository.save(newLeaveManagement);
                        });

                leaveManagement.setLeaveBalance(
                        leaveManagement.getLeaveBalance() + leaveType.getMonthlyAccrual());

                leaveManagementRepository.save(leaveManagement);
            }
        }
    }

    @Transactional
    public void carryForwardLeave() {
        List<LeaveType> leaveTypes = leaveTypeRepo.findAll();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            for (LeaveType leaveType : leaveTypes) {
                LeaveManagement leaveManagement = leaveMngtRepo
                        .findByUserAndLeaveType(user, leaveType)
                        .orElse(null);

                if (leaveManagement != null) {
                    Double leaveCarryForward = leaveManagement.getLeaveType().getMaxCarryForward(); // initially they have to be five
                    if (leaveManagement.getLeaveBalance() > leaveCarryForward) {
                        leaveManagement.setLeaveBalance(leaveCarryForward);
                        leaveMngtRepo.save(leaveManagement);
                    }
                }
            }
        }
    }
}
