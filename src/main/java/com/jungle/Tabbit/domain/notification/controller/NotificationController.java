package com.jungle.Tabbit.domain.notification.controller;

import com.jungle.Tabbit.domain.notification.service.NotificationService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;


    @DeleteMapping("/delete")
    public CommonResponse<?> deleteAllNotification(@AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.deleteALLNotification(userDetails.getUserId());
        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }

    @DeleteMapping("/delete/{notificationId}")
    public CommonResponse<?> deleteNotification(@AuthenticationPrincipal CustomUserDetails userDetails, Long notificationId) {
        notificationService.deleteNotification(userDetails.getUserId(), notificationId);
        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }

    @PutMapping("/check")
    public CommonResponse<?> checkNotification(@AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.checkNotification(userDetails.getUserId());
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @GetMapping("/list")
    public CommonResponse<?> getNotificationList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, notificationService.getNotificationList(userDetails.getUserId()));
    }


}
