package com.jungle.Tabbit.domain.waiting.controller;

import com.jungle.Tabbit.domain.waiting.dto.WaitingRequestCreateDto;
import com.jungle.Tabbit.domain.waiting.dto.WaitingResponseDto;
import com.jungle.Tabbit.domain.waiting.service.WaitingService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<WaitingResponseDto> waitingList = waitingService.getUserWaitingList(userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, waitingList);
    }
}