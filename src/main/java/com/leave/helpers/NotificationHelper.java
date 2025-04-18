package com.leave.helpers;

import com.leave.model.Leave;
import com.leave.model.Notification;
import com.leave.model.User;
import com.leave.repository.NotificationRepository;
import com.leave.shared.enums.LeaveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificationHelper {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendLeaveStatusNotification(Leave leave, User approver, LeaveStatus newStatus) {
        // Send email notification
        sendEmailNotification(leave, approver, newStatus);
        
        // Send in-app notification
        sendInAppNotification(leave, approver, newStatus);
    }

    private void sendEmailNotification(Leave leave, User approver, LeaveStatus newStatus) {
        String subject = "Leave Request Status Update";
        String recipientEmail = leave.getUser().getEmail();
        String approverName = approver.getDisplayName();
        String employeeName = leave.getUser().getDisplayName();
        
        String message = String.format(
            "Dear %s,\n\n" +
            "Your leave request has been %s by %s.\n" +
            "Leave Type: %s\n" +
            "Reason: %s\n\n" +
            "Best regards,\n" +
            "Leave Management System",
            employeeName,
            newStatus.toString().toLowerCase(),
            approverName,
            leave.getType(),
            leave.getLeaveReason()
        );

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);
        
        try {
            mailSender.send(email);
        } catch (Exception e) {
            // Log the error but don't throw it to prevent affecting the main flow
            System.err.println("Failed to send email notification: " + e.getMessage());
        }
    }

    private void sendInAppNotification(Leave leave, User approver, LeaveStatus newStatus) {
        Notification notification = new Notification();
        notification.setUser(leave.getUser());
        notification.setTitle("Leave Request Status Update");
        
        String message = String.format(
            "Your leave request has been %s by %s",
            newStatus.toString().toLowerCase(),
            approver.getDisplayName()
        );
        notification.setMessage(message);
        
        try {
            notificationRepository.save(notification);
        } catch (Exception e) {
            // Log the error but don't throw it to prevent affecting the main flow
            System.err.println("Failed to save in-app notification: " + e.getMessage());
        }
    }
} 