package com.jungle.Tabbit.domain.stampBadge.controller;

import com.jungle.Tabbit.domain.stampBadge.dto.StampResponseListDto;
import com.jungle.Tabbit.domain.stampBadge.service.StampService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stamp")
public class StampController {

    private final StampService stampService;

    @GetMapping
    @Operation(summary = "스탬프 전체 조회", description = "사용자의 모든 스탬프를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스탬프 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StampResponseListDto.class))),
    })
    public CommonResponse<?> getStampAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, stampService.getStampAll(userDetails.getUsername()));
    }

    @GetMapping("/user")
    @Operation(summary = "유저의 모든 스탬프 조회", description = "특정 유저가 가진 모든 스탬프를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저의 모든 스탬프 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StampResponseListDto.class))),
    })
    public CommonResponse<?> getStampAllByUser(@RequestParam String username) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, stampService.getStampAll(username));
    }
}