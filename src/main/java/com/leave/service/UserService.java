package com.leave.service;

import com.leave.dto.*;
import com.leave.model.User;
import com.leave.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public SignInResponse signIn(SignInRequest request) {
        // Validate email domain
        if (!request.getEmail().endsWith("@ist.com")) {
            throw new IllegalArgumentException("Only @ist.com email addresses are allowed");
        }

        // Check if user exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            //TODO: In future accept to update the user details by adding microsoftId, displayName, avatarUrl
            user = userRepository.save(user);
            return mapToSignInResponse(user);
        }else{
            throw new IllegalArgumentException("User not found");
        }
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setDisplayName(user.getDisplayName());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setRole(user.getRole());
        response.setTeam(new TeamResponse(user.getTeam()));
        response.setActive(user.isActive());
        return response;
    }

    private SignInResponse mapToSignInResponse(User user) {
        SignInResponse response = new SignInResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setDisplayName(user.getDisplayName());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setRole(user.getRole());
        response.setTeam(new TeamResponse(user.getTeam()));
        response.setActive(user.isActive());
        return response;
    }
} 