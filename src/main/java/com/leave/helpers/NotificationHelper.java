package com.leave.helpers;

import com.leave.model.Leave;
import com.leave.model.Notification;
import com.leave.model.User;
import com.leave.repository.NotificationRepository;
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

    public void sendLeaveStatusNotification(Leave leave, User approver, String newStatus) {
       
        sendInAppNotification(leave, approver, newStatus);
        //sendEmailNotification(leave, approver, newStatus);
         
    }

    private void sendEmailNotification(Leave leave, User approver, String newStatus) {
        String subject = "Leave Request Status Update";
        String recipientEmail = leave.getUser().getEmail();
        String approverName = approver.getName();
        String employeeName = leave.getUser().getName();
        
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

    private void sendInAppNotification(Leave leave, User approver, String newStatus) {
        Notification notification = new Notification();
        notification.setUser(leave.getUser());
        notification.setTitle("Leave Request Status Update");
        
        String message = String.format(
            "A leave request has been %s by %s",
            newStatus.toString().toLowerCase(),
            approver.getName()
        );
        notification.setMessage(message);
        
        try {
            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Failed to save in-app notification: " + e.getMessage());
        }
    }
} 