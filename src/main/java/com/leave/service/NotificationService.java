package com.leave.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.leave.dto.notification.NotificationResponse;
import com.leave.model.Notification;
import com.leave.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;


    public List<NotificationResponse> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll(); 
        return formatNotificationResponse(notifications);
        
    }
    public List<NotificationResponse> getUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId); 
        return formatNotificationResponse(notifications);
        
    }
    public String updateNotificationStatus(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);

        for (Notification notification : notifications) {
            notification.setRead(true); // Mark as read
            notificationRepository.save(notification); // Save the updated notification
        }

        return "All notifications marked as read";
    }
    public List<NotificationResponse> formatNotificationResponse(List<Notification> notifications){
       return notifications.stream().map(notification -> {
            NotificationResponse response = new NotificationResponse();
            response.setId(notification.getId());
            response.setTitle(notification.getTitle());
            response.setMessage(notification.getMessage());
            response.setCreatedAt(notification.getCreatedAt());
            response.setIsRead(notification.isRead());
            response.setUserId(notification.getUser().getId());
            return response;
        }).collect(Collectors.toList());
    }
}