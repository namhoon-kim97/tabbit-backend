package com.jungle.Tabbit.domain.waiting.controller;

import com.jungle.Tabbit.domain.waiting.dto.WaitingRequestCreateDto;
import com.jungle.Tabbit.domain.waiting.dto.WaitingResponseDto;
import com.jungle.Tabbit.domain.waiting.service.WaitingService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}