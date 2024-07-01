package com.jungle.Tabbit.domain.stampBadge.controller;

import com.jungle.Tabbit.domain.stampBadge.dto.BadgeResponseListDto;
import com.jungle.Tabbit.domain.stampBadge.dto.UserWithBadgeResponseListDto;
import com.jungle.Tabbit.domain.stampBadge.service.BadgeService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/badge")
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping
    @Operation(summary = "칭호 전체 조회", description = "사용자의 모든 칭호를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "칭호 조회 성공", content = @Content(schema = @Schema(implementation = BadgeResponseListDto.class)))
    public CommonResponse<?> getBadgeAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, badgeService.getBadgeAll(userDetails.getUsername()));
    }

    @GetMapping("/{username}")
    @Operation(summary = "유저의 모든 칭호 조회", description = "특정 유저가 가진 모든 칭호를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "유저의 모든 칭호 조회 성공", content = @Content(schema = @Schema(implementation = BadgeResponseListDto.class)))
    public CommonResponse<?> getBadgeAllByUser(@PathVariable String username) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, badgeService.getBadgeAll(username));
    }

    @GetMapping("/users-with-badge/{badgeId}")
    @Operation(summary = "칭호를 가진 유저 목록 조회", description = "특정 칭호를 가진 모든 유저의 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "칭호를 가진 유저 목록 조회 성공", content = @Content(schema = @Schema(implementation = UserWithBadgeResponseListDto.class)))
    public CommonResponse<?> getUsersWithBadge(@PathVariable Long badgeId) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, badgeService.getUsersWithBadge(badgeId));
    }
}
