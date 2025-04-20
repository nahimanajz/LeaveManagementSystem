package com.leave.repository;

import com.leave.model.Leave;
import com.leave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByUser(User user);
    List<Leave> findByApprover(User approver);
    List<Leave> findByApprovalStatus(String status);
    List<Leave> findByUserId(Long userId);
} 