package com.jungle.Tabbit.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {

    private Long memberId;

    private Long notificationId;

    private String title;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private boolean isRead;

    public static NotificationResponseDto of( Notification notification) {
        return NotificationResponseDto.builder()
                .memberId(notification.getMember().getMemberId())
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.isRead())
                .build();
    }
}
