package com.leave.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.leave.dto.ErrorResponse;
import com.leave.dto.notification.NotificationResponse;
import com.leave.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notifService;

    @GetMapping()
    public ResponseEntity<?> getAllNotifications() {
        try {
            List<NotificationResponse> notifications = notifService.getAllNotifications();
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Internal server error", "SERVER_ERROR"));
        }
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserNotifications(@PathVariable Long userId) {
        try {
            List<NotificationResponse> notifications = notifService.getUserNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Internal server error", "SERVER_ERROR"));
        }
    }


    @PutMapping("/{userId}/mark-as-read")
    public ResponseEntity<String> updateNotificationStatus(@PathVariable Long userId) {
        try {
            String notifications = notifService.updateNotificationStatus(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Failed to mark notifications as read");
        }
    }
}