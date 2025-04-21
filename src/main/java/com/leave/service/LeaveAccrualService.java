package com.leave.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.model.User;
import com.leave.repository.LeaveTypeRepository;
import com.leave.repository.UserRepository;

@Service
public class LeaveAccrualService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void autoAccrueLeave() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            // Accrue 1.66 days per month
            user.setRemainingLeaveDays(user.getRemainingLeaveDays() + 1.66);
            userRepository.save(user);
        }
    }

    @Transactional
    public void carryForwardLeave() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getRemainingLeaveDays() > 5) {
                user.setRemainingLeaveDays(5); // Cap carry-forward to 5 days
            }
            userRepository.save(user);
        }
    }
}
