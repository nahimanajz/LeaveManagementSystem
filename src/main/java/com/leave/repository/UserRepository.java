package com.leave.repository;

import com.leave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMicrosoftId(String microsoftId);
    List<User> findByTeamId(Long teamId);
    List<User> findByIsActiveTrue();
    boolean existsByEmail(String email);
} 