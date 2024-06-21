package com.jungle.Tabbit.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationRequestCreateDto {
    private Long MemberId;
    private String title;
    private String message;
}
