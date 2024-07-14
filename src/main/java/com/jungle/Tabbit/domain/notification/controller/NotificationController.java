package com.jungle.Tabbit.domain.notification.controller;

import com.jungle.Tabbit.domain.notification.dto.NotificationListResponseDto;
import com.jungle.Tabbit.domain.notification.dto.NotificationResponseDto;
import com.jungle.Tabbit.domain.notification.service.NotificationService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;


    @DeleteMapping
    public CommonResponse<?> deleteAllNotification(@AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.deleteALLNotification(userDetails.getUserId());
        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }

    @DeleteMapping("/{notificationId}")
    public CommonResponse<?> deleteNotification(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long notificationId) {
        notificationService.deleteNotification(userDetails.getUserId(), notificationId);
        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }

    @PutMapping("/{notificationId}")
    public CommonResponse<?> checkNotification(@PathVariable Long notificationId) {
        notificationService.checkNotification(notificationId);
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @GetMapping
    public CommonResponse<?> getNotificationList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<NotificationResponseDto> notificationList = notificationService.getNotificationList(userDetails.getUserId());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, notificationList);
    }

}
