package com.jungle.Tabbit.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jungle.Tabbit.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {

    private Long notificationId;

    private String title;

    private String message;

    private String target;

    private String messageType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private boolean isRead;

    public static NotificationResponseDto of( Notification notification) {
        return NotificationResponseDto.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .target(notification.getTarget())
                .messageType(notification.getMessageType())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.isRead())
                .build();
    }
}
