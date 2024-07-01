package com.jungle.Tabbit.domain.stampBadge.controller;

import com.jungle.Tabbit.domain.stampBadge.dto.BadgeTriggerRequestDto;
import com.jungle.Tabbit.domain.stampBadge.service.BadgeTriggerService;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/badge-trigger")
public class BadgeTriggerController {
    private final BadgeTriggerService badgeTriggerService;

    @PostMapping
    @Operation(summary = "BadgeTrigger 추가", description = "필요한 BadgeTrigger를 추가합니다.")
    @ApiResponse(responseCode = "200", description = "BadgeTrigger 추가 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> addBadgeTrigger(@RequestBody BadgeTriggerRequestDto requestDto) {
        badgeTriggerService.addBadgeTrigger(requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
    }
}