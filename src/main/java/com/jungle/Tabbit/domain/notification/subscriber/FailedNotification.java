package com.jungle.Tabbit.domain.notification.subscriber;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FailedNotification {
    private String originalMessage;
    private String errorMessage;
    private String failedAt;
}
