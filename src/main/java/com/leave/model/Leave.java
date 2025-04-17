package com.leave.model;

import com.leave.shared.enums.LeaveStatus;
import com.leave.shared.enums.LeaveType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "leaves")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(name = "approver_comment")
    private String approverComment;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LeaveType type;

    @Column(name = "leave_reason", nullable = false)
    private String leaveReason;

    @Column(name = "is_full_day", nullable = false)
    private boolean isFullDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private LeaveStatus approvalStatus = LeaveStatus.PENDING;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "document_name")
    private String documentName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
} 