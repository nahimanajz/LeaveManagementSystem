package com.leave.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "leave_managements")
public class LeaveManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "leave_balance", nullable = true)
    private Double leaveBalance; 
}