package com.leave.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "leave_types")
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    
    
    @Column(name = "description", nullable = false)
    private String description;
    
    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "default_days", nullable = false)
    private Double defaultDays = 20.0;

    @Column(name = "is_Active", nullable = false)
    private boolean isActive;

    @Column(name = "monthly_accrual", nullable = false)
    private Double monthlyAccrual =1.66;

    @Column(name = "max_carry_forward", nullable = false)
    private Double maxCarryForward =5.0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
} 