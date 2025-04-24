package com.leave.service;

import com.leave.dto.*;
import com.leave.dto.LeaveManagement.UpdateLeaveBalanceRequest;
import com.leave.dto.user.AdminOrManagerRequest;
import com.leave.dto.user.SignupRequest;
import com.leave.model.LeaveManagement;
import com.leave.model.User;
import com.leave.repository.LeaveManagementRepository;
import com.leave.repository.UserRepository;
import com.leave.shared.enums.UserRole;
import com.leave.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwt;

    @Autowired
    private LeaveManagementRepository leaveMngtRepo;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse signIn(SignInRequest request) {
        // Validate email domain
        if (!request.getEmail().endsWith("@ist.com")) {
            throw new IllegalArgumentException("Only @ist.com email addresses are allowed");
        }

        // Check if user exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Verify the password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid password");
            }

            return mapToUserResponse(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Transactional
    public UserResponse adminOrManagerSignup(AdminOrManagerRequest request) {
        // Hash the password
        if (!request.getEmail().endsWith("@ist.com")) {
            throw new IllegalArgumentException("Only @ist.com email addresses are allowed");
        }
        // Check if user already exists

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return mapToUserResponse(existingUser.get());
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Save the admin to the database
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setRole(request.getRole());
        user.setName(request.getName());
        user.setDepartment("ist-management-team");
        user.setPosition("one-of-ist-management-position");
        user.setStartDate(LocalDate.now());
        user.setActive(true);
        userRepository.save(user);

        return mapToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    // create signup method
    @Transactional
    public UserResponse signUp(SignupRequest request) {
        // Validate email domain
        if (!request.getEmail().trim().toLowerCase().endsWith("@ist.com")
                && !request.getEmail().trim().toLowerCase().endsWith("@outlook.com")) {
            throw new IllegalArgumentException(
                    "Only @ist.com or @outlook.com email addresses are allowed: " + request.getEmail());
        }
        // Check if user already exists

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return mapToUserResponse(existingUser.get());
        }
        // Check if microsoftId already exists

        Optional<User> existingUserByMicrosoftId = userRepository.findByMicrosoftId(request.getMicrosoftId());
        if (existingUserByMicrosoftId.isPresent()) {
            return mapToUserResponse(existingUserByMicrosoftId.get());
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPosition(request.getPosition());
        user.setDepartment(request.getDepartment());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setMicrosoftId(request.getMicrosoftId());
        user.setActive(true);
        user.setStartDate(request.getStartDate());

        // Set role based on email domain
        if (request.getEmail().trim().toLowerCase().endsWith("@ist.com")) {
            user.setRole(request.getRole());
        } else {
            user.setRole(UserRole.STAFF);
        }

        // Save the user
        user = userRepository.save(user);

        // Map user to response
        return mapToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmailOrMicrosoftId(String email, String microsoftId) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isPresent() && microsoftId != null) {
            userOptional = userRepository.findByMicrosoftId(microsoftId);
        }

        if (userOptional.isPresent()) {
            return mapToUserResponse(userOptional.get());
        } else {
            throw new IllegalArgumentException("User not found with the provided email or Microsoft ID");
        }
    }

    public UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setRole(user.getRole());
        response.setDepartment(user.getDepartment());
        response.setPosition(user.getPosition());
        response.setActive(user.isActive());
        response.setMicrosoftId(user.getMicrosoftId());
        response.setCreatedAt(user.getCreatedAt().toString());
        response.setUpdatedAt(user.getUpdatedAt().toString());
        response.setToken(jwt.generateToken(user.getEmail()));
        response.setLeaveBalances(getUserWithLeaveBalances(user));
        return response;
    }

    private Map<String, Double> getUserWithLeaveBalances(User user) {
        // Fetch leave balances for the user
        List<LeaveManagement> leaveManagements = leaveMngtRepo.findByUser(user);

        // Map leave balances to a Map<String, Double>
        Map<String, Double> leaveBalances = leaveManagements.stream()
                .collect(Collectors.toMap(
                        lm -> lm.getLeaveType().getName(),
                        LeaveManagement::getLeaveBalance));
        return leaveBalances;
    }

    @Transactional
    public UserResponse updateLeaveBalance(Long userId, UpdateLeaveBalanceRequest request) {
        
        Optional<LeaveManagement> lm = leaveMngtRepo.findByUserIdAndLeaveTypeId(userId, request.getLeaveTypeId());
        if (lm == null) {
            throw new RuntimeException(
                    "Leave record not found for userId " + userId + " and leaveTypeId " + request.getLeaveTypeId());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        LeaveManagement actualLm = lm.get();
        double updatedBalance = actualLm.getLeaveBalance() + request.getLeaveBalance();

        actualLm.setLeaveBalance(updatedBalance);

        actualLm.setUser(user);
        leaveMngtRepo.save(actualLm);
        return mapToUserResponse(user);

    }
}