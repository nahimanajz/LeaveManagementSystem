package com.leave.dto.notification;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private Boolean isRead;
    private Long userId;
}
