package com.jungle.Tabbit.domain.waiting.controller;

import com.jungle.Tabbit.domain.waiting.dto.WaitingRequestCreateDto;
import com.jungle.Tabbit.domain.waiting.dto.WaitingResponseDto;
import com.jungle.Tabbit.domain.waiting.service.WaitingService;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/waitings")
@RequiredArgsConstructor
public class WaitingController {
    private WaitingService waitingService;

    @PostMapping("/{nfcId}")
    public CommonResponse<?> registerWaiting(@RequestBody WaitingRequestCreateDto requestDto, @PathVariable("nfcId") String nfcId) {
        WaitingResponseDto responseDto = waitingService.registerWaiting(requestDto, nfcId);
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE, responseDto);
    }
}