package com.jungle.Tabbit.domain.waiting.controller;

import com.jungle.Tabbit.domain.waiting.dto.*;
import com.jungle.Tabbit.domain.waiting.service.WaitingService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/waitings")
@RequiredArgsConstructor
public class WaitingController {
    private final WaitingService waitingService;

    @PostMapping
    public CommonResponse<?> registerWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody WaitingRequestCreateDto requestDto) {
        WaitingResponseDto responseDto = waitingService.registerWaiting(requestDto, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE, responseDto);
    }

    @GetMapping("/{restaurantId}")
    public CommonResponse<?> getWaitingOverview(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long restaurantId) {
        WaitingResponseDto responseDto = waitingService.getWaitingOverview(restaurantId, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @GetMapping("/list")
    public CommonResponse<?> getUserWaitingList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        WaitingListResponseDto responseDto = waitingService.getUserWaitingList(userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @PutMapping("/{restaurantId}/cancel")
    public CommonResponse<?> cancelWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long restaurantId) {
        waitingService.cancelWaiting(restaurantId, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }

    @PutMapping("/{restaurantId}/confirm")
    public CommonResponse<?> confirmWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long restaurantId, @RequestBody WaitingRequestUpdateDto requestDto) {
        waitingService.confirmWaiting(restaurantId, userDetails.getUsername(), requestDto.getWaitingNumber());
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @PutMapping("/{restaurantId}/call")
    public CommonResponse<?> callWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long restaurantId, @RequestBody WaitingRequestUpdateDto requestDto) {
        waitingService.callWaiting(restaurantId, userDetails.getUsername(), requestDto.getWaitingNumber());
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @PutMapping("/{restaurantId}/no-show")
    public CommonResponse<?> noShowWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long restaurantId, @RequestBody WaitingRequestUpdateDto requestDto) {
        waitingService.noShowWaiting(restaurantId, userDetails.getUsername(), requestDto.getWaitingNumber());
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @GetMapping("/{restaurantId}/owner-list")
    public CommonResponse<?> getOwnerWaitingList(@PathVariable Long restaurantId) {
        OwnerWaitingListResponseDto responseDto = waitingService.getOwnerWaitingList(restaurantId);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }
}