package com.jungle.Tabbit.domain.notification.dto;

import com.jungle.Tabbit.domain.fcm.dto.FcmData;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationRequestCreateDto {
    private Long memberId;
    private String title;
    private String message;

    private FcmData fcmData;

    @Override
    public String toString() {
        return "NotificationRequestCreateDto{" +
                "memberId=" + memberId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", fcmData=" + fcmData +
                '}';
    }
}
