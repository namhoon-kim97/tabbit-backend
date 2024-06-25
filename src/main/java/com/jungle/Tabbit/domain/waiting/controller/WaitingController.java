package com.jungle.Tabbit.domain.waiting.controller;

import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseSummaryDto;
import com.jungle.Tabbit.domain.waiting.dto.*;
import com.jungle.Tabbit.domain.waiting.service.WaitingService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "웨이팅 등록 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = WaitingResponseDto.class)))
    })
    public CommonResponse<?> registerWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Parameter(description = "웨이팅 요청 DTO", required = true) WaitingRequestCreateDto requestDto) {
        WaitingResponseDto responseDto = waitingService.registerWaiting(requestDto, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE, responseDto);
    }

    @GetMapping("/{restaurantId}")
    @Operation(summary = "웨이팅 개요 조회", description = "특정 맛집의 웨이팅 개요를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웨이팅 개요 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = WaitingResponseDto.class)))
    })
    public CommonResponse<?> getWaitingOverview(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId) {
        WaitingResponseDto responseDto = waitingService.getWaitingOverview(restaurantId, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @GetMapping("/list")
    @Operation(summary = "사용자의 웨이팅 목록 조회", description = "사용자의 웨이팅 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자의 웨이팅 목록 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = WaitingListResponseDto.class)))
    })
    public CommonResponse<?> getUserWaitingList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        WaitingListResponseDto responseDto = waitingService.getUserWaitingList(userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @PutMapping("/{restaurantId}/cancel")
    @Operation(summary = "웨이팅 취소", description = "특정 맛집의 웨이팅을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웨이팅 취소 성공")
    })
    public CommonResponse<?> cancelWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId) {
        waitingService.cancelWaiting(restaurantId, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }

    @PutMapping("/{restaurantId}/confirm")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "웨이팅 확정", description = "특정 맛집의 웨이팅을 확정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웨이팅 확정 성공")
    })
    public CommonResponse<?> confirmWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId, @RequestBody @Parameter(description = "웨이팅 번호 요청 DTO", required = true) WaitingRequestUpdateDto requestDto) {
        waitingService.confirmWaiting(restaurantId, userDetails.getUsername(), requestDto.getWaitingNumber());
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @PutMapping("/{restaurantId}/call")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "웨이팅 호출", description = "특정 맛집의 웨이팅을 호출합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웨이팅 호출 성공")
    })
    public CommonResponse<?> callWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId, @RequestBody @Parameter(description = "웨이팅 번호 요청 DTO", required = true) WaitingRequestUpdateDto requestDto) {
        waitingService.callWaiting(restaurantId, userDetails.getUsername(), requestDto.getWaitingNumber());
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @PutMapping("/{restaurantId}/no-show")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "웨이팅 노쇼 처리", description = "특정 맛집의 웨이팅을 노쇼 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웨이팅 노쇼 처리 성공")
    })
    public CommonResponse<?> noShowWaiting(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId, @RequestBody @Parameter(description = "웨이팅 번호 요청 DTO", required = true) WaitingRequestUpdateDto requestDto) {
        waitingService.noShowWaiting(restaurantId, userDetails.getUsername(), requestDto.getWaitingNumber());
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @GetMapping("/{restaurantId}/owner-list")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "맛집의 웨이팅 목록 조회", description = "특정 맛집의 웨이팅 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "맛집의 웨이팅 목록 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OwnerWaitingListResponseDto.class)))
    })
    public CommonResponse<?> getOwnerWaitingList(@PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId) {
        OwnerWaitingListResponseDto responseDto = waitingService.getOwnerWaitingList(restaurantId);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @PostMapping("/nfc")
    @Operation(summary = "가게 정보 조회", description = "nfc 태그 시 가게 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RestaurantResponseSummaryDto.class)))
    })
    public CommonResponse<?> getTagInfo(@RequestBody @Parameter(description = "요청 DTO", required = true) WaitingNfcRequestDto requestDto) {
        RestaurantResponseSummaryDto responseDto = waitingService.getTagInfo(requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }
}
