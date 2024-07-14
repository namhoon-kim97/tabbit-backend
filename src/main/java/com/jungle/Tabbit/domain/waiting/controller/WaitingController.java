package com.jungle.Tabbit.domain.waiting.controller;

import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseSummaryDto;
import com.jungle.Tabbit.domain.waiting.dto.*;
import com.jungle.Tabbit.domain.waiting.service.WaitingService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/waitings")
@RequiredArgsConstructor
public class WaitingController {
    private final WaitingService waitingService;

    @PostMapping
    @Operation(summary = "웨이팅 등록", description = "새로운 웨이팅을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "웨이팅 등록 성공", content = @Content(schema = @Schema(implementation = WaitingResponseDto.class)))
    public CommonResponse<?> registerWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Parameter(description = "웨이팅 요청 DTO", required = true) WaitingRequestCreateDto requestDto) {
        WaitingResponseDto responseDto = waitingService.registerWaiting(requestDto, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE, responseDto);
    }

    @GetMapping("/{waitingId}")
    @Operation(summary = "웨이팅 개요 조회", description = "특정 맛집의 웨이팅 개요를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "웨이팅 개요 조회 성공", content = @Content(schema = @Schema(implementation = WaitingResponseDto.class)))
    public CommonResponse<?> getWaitingOverview(@PathVariable @Parameter(description = "웨이팅 ID", required = true) Long waitingId) {
        WaitingResponseDto responseDto = waitingService.getWaitingOverview(waitingId);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @GetMapping("/list")
    @Operation(summary = "사용자의 웨이팅 목록 조회", description = "사용자의 웨이팅 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자의 웨이팅 목록 조회 성공", content = @Content(schema = @Schema(implementation = WaitingListResponseDto.class)))
    public CommonResponse<?> getUserWaitingList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        WaitingListResponseDto responseDto = waitingService.getUserWaitingList(userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @PutMapping("/{waitingId}/cancel")
    @Operation(summary = "웨이팅 취소", description = "특정 맛집의 웨이팅을 취소합니다.")
    @ApiResponse(responseCode = "200", description = "웨이팅 취소 성공")
    public CommonResponse<?> cancelWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "웨이팅 ID", required = true) Long waitingId) {
        waitingService.cancelWaiting(waitingId, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }

    @PutMapping("/{waitingId}/confirm")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "웨이팅 확정", description = "특정 맛집의 웨이팅을 확정합니다.")
    @ApiResponse(responseCode = "200", description = "웨이팅 확정 성공")
    public CommonResponse<?> confirmWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "웨이팅 ID", required = true) Long waitingId) {
        waitingService.confirmWaiting(waitingId, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @PutMapping("/{waitingId}/call")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "웨이팅 호출", description = "특정 맛집의 웨이팅을 호출합니다.")
    @ApiResponse(responseCode = "200", description = "웨이팅 호출 성공")
    public CommonResponse<?> callWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "웨이팅 ID", required = true) Long waitingId) {
        waitingService.callWaiting(waitingId, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @PutMapping("/{waitingId}/no-show")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "웨이팅 노쇼 처리", description = "특정 맛집의 웨이팅을 노쇼 처리합니다.")
    @ApiResponse(responseCode = "200", description = "웨이팅 노쇼 처리 성공")
    public CommonResponse<?> noShowWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "웨이팅 ID", required = true) Long waitingId) {
        waitingService.noShowWaiting(waitingId, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @GetMapping("/{restaurantId}/owner-list")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "맛집의 웨이팅 목록 조회", description = "특정 맛집의 웨이팅 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "맛집의 웨이팅 목록 조회 성공", content = @Content(schema = @Schema(implementation = OwnerWaitingListResponseDto.class)))
    public CommonResponse<?> getOwnerWaitingList(@PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId) {
        OwnerWaitingListResponseDto responseDto = waitingService.getOwnerWaitingList(restaurantId);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @PostMapping("/nfc")
    @Operation(summary = "가게 정보 조회", description = "nfc 태그 시 가게 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "정보 조회 성공", content = @Content(schema = @Schema(implementation = RestaurantResponseSummaryDto.class)))
    public CommonResponse<?> getTagInfo(@RequestBody @Parameter(description = "요청 DTO", required = true) WaitingNfcRequestDto requestDto) {
        RestaurantResponseSummaryDto responseDto = waitingService.getTagInfo(requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }
}
