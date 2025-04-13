package com.jungle.Tabbit.domain.notification.subscriber;

import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import com.jungle.Tabbit.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationHandler {

    private final NotificationService notificationService;

    @Async("taskExecutor")
    public void handle(NotificationRequestCreateDto requestDto) {
        notificationService.sendNotification(requestDto, false);
    }
}

