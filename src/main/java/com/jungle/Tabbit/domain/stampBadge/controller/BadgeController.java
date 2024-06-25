package com.jungle.Tabbit.domain.stampBadge.controller;

import com.jungle.Tabbit.domain.stampBadge.dto.BadgeResponseListDto;
import com.jungle.Tabbit.domain.stampBadge.service.BadgeService;
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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/badge")
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping
    @Operation(summary = "칭호 전체 조회", description = "사용자의 모든 칭호를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "칭호 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = BadgeResponseListDto.class))),
    })
    public CommonResponse<?> getBadgeAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, badgeService.getBadgeAll(userDetails.getUsername()));
    }
}
