package com.leave.service;

import com.leave.model.LeaveBalance;
import com.leave.model.User;
import com.leave.repository.LeaveBalanceRepository;
import com.leave.repository.UserRepository;
import com.leave.shared.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Service
public class LeaveBalanceService {
    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public LeaveBalance getOrCreateBalance(User user, Integer year) {
        return leaveBalanceRepository.findByUserAndYear(user, year)
                .orElseGet(() -> createNewBalance(user, year));
    }

    private LeaveBalance createNewBalance(User user, Integer year) {
        LeaveBalance balance = new LeaveBalance();
        balance.setUser(user);
        balance.setYear(year);
        balance.setTotalBalance(LeaveBalance.DEFAULT_BALANCE);
        return leaveBalanceRepository.save(balance);
    }

    @Scheduled(cron = "0 0 0 1 * ?") // Run at midnight on the 1st of every month
    @Transactional
    public void processMonthlyAccrual() {
        int currentYear = Year.now().getValue();
        List<User> activeUsers = userRepository.findByIsActiveTrue();

        for (User user : activeUsers) {
            LeaveBalance balance = getOrCreateBalance(user, currentYear);
            balance.setAccruedBalance(balance.getAccruedBalance() + LeaveBalance.MONTHLY_ACCRUAL);
            balance.setTotalBalance(balance.getTotalBalance() + LeaveBalance.MONTHLY_ACCRUAL);
            balance.setLastAccrualDate(LocalDateTime.now());
            leaveBalanceRepository.save(balance);
        }
    }

    @Scheduled(cron = "0 0 0 1 1 ?") // Run at midnight on January 1st
    @Transactional
    public void processYearEndCarryForward() {
        int previousYear = Year.now().getValue() - 1;
        int currentYear = Year.now().getValue();
        List<LeaveBalance> previousYearBalances = leaveBalanceRepository.findByYear(previousYear);

        for (LeaveBalance previousBalance : previousYearBalances) {
            double remainingBalance = previousBalance.getTotalBalance() - previousBalance.getUsedBalance();
            double carryForward = Math.min(remainingBalance, LeaveBalance.MAX_CARRY_FORWARD);
            double expired = Math.max(0, remainingBalance - LeaveBalance.MAX_CARRY_FORWARD);

            LeaveBalance newBalance = getOrCreateBalance(previousBalance.getUser(), currentYear);
            newBalance.setCarriedForward(carryForward);
            newBalance.setExpiredBalance(expired);
            newBalance.setTotalBalance(newBalance.getTotalBalance() + carryForward);
            leaveBalanceRepository.save(newBalance);
        }
    }

    @Transactional
    public void adjustBalance(Long userId, Integer year, Double adjustment, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Only allow HR/Admin to make manual adjustments
        if (user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only HR and Admin can make manual adjustments");
        }

        LeaveBalance balance = getOrCreateBalance(user, year);
        balance.setTotalBalance(balance.getTotalBalance() + adjustment);
        leaveBalanceRepository.save(balance);
    }

    @Transactional
    public void updateUsedBalance(User user, Integer year, Double daysUsed) {
        LeaveBalance balance = getOrCreateBalance(user, year);
        balance.setUsedBalance(balance.getUsedBalance() + daysUsed);
        leaveBalanceRepository.save(balance);
    }
} 