package com.leave.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.leave.shared.enums.LeaveType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "leave_balances")
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
  

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "total_balance", nullable = false)
    private Double totalBalance = 20.0; // Default 20 days

    @Column(name = "accrued_balance", nullable = false)
    private Double accruedBalance = 0.0;

    @Column(name = "carried_forward", nullable = false)
    private Double carriedForward = 0.0;

    @Column(name = "used_balance", nullable = false)
    private Double usedBalance = 0.0;

    @Column(name = "expired_balance", nullable = false)
    private Double expiredBalance = 0.0;

    @Column(name = "last_accrual_date")
    private LocalDateTime lastAccrualDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constants
    public static final double MONTHLY_ACCRUAL = 1.66;
    public static final double MAX_CARRY_FORWARD = 5.0;
    public static final double DEFAULT_BALANCE = 20.0;
} 