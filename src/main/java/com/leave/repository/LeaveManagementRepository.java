package com.leave.repository;

import com.leave.model.LeaveManagement;
import com.leave.model.LeaveType;
import com.leave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveManagementRepository extends JpaRepository<LeaveManagement, Long> {
    Optional<LeaveManagement> findByUserAndLeaveType(User user, LeaveType leaveType);
    List<LeaveManagement> findByUser(User user);

}