package com.leave.repository;

import com.leave.model.LeaveBalance;
import com.leave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByUserAndYear(User user, Integer year);
    List<LeaveBalance> findByUserOrderByYearDesc(User user);
    List<LeaveBalance> findByYear(Integer year);
} 