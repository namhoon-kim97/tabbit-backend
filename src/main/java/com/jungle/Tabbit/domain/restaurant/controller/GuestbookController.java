package com.jungle.Tabbit.domain.restaurant.controller;

import com.jungle.Tabbit.domain.restaurant.dto.guestbook.GuestbookRequestDto;
import com.jungle.Tabbit.domain.restaurant.dto.guestbook.GuestbookResponseListDto;
import com.jungle.Tabbit.domain.restaurant.service.GuestbookService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guestbook")
@Tag(name = "Guestbook API", description = "방명록 관련 API")
public class GuestbookController {

    private final GuestbookService guestbookService;

    @GetMapping("/{restaurantId}")
    @Operation(summary = "방명록 조회", description = "해당 맛집의 방명록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = GuestbookResponseListDto.class)))
    public CommonResponse<?> getGuestbookAll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId,
                                             @RequestParam @Parameter(description = "페이지 number", required = true) Long pageNumber,
                                             @RequestParam @Parameter(description = "페이지 size", required = true) Long pageSize) {
        GuestbookResponseListDto responseDto = guestbookService.getAllGuestbook(userDetails.getUsername(), restaurantId, pageNumber, pageSize);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @PostMapping("/{restaurantId}")
    @Operation(summary = "방명록 등록", description = "해당 맛집의 새로운 방명록을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "등록 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> createGuestbook(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId,
                                             @RequestBody @Parameter(description = "방명록 등록 요청 DTO", required = true) GuestbookRequestDto requestDto) {
        guestbookService.createGuestbook(userDetails.getUsername(), restaurantId, requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }

    @DeleteMapping("/{restaurantId}/{guestbookId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "방명록 삭제", description = "해당 맛집의 방명록을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> createGuestbook(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId,
                                             @PathVariable @Parameter(description = "방명록 ID", required = true) Long guestbookId) {
        guestbookService.deleteGuestbook(userDetails.getUsername(), restaurantId, guestbookId);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
    }
}
