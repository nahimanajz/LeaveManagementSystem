package com.leave.model;

import com.leave.shared.enums.LeaveType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "leave_policies")
public class LeavePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = true)
    private LeaveType type;

    @Column(name = "default_balance", nullable = false)
    private Double defaultBalance;

    @Column(name = "monthly_accrual", nullable = false)
    private Double monthlyAccrual;

    @Column(name = "max_carry_forward", nullable = false)
    private Double maxCarryForward;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
} 