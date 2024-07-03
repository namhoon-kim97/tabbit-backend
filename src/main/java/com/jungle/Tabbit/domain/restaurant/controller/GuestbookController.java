package com.jungle.Tabbit.domain.restaurant.controller;

import com.jungle.Tabbit.domain.image.service.ImageService;
import com.jungle.Tabbit.domain.restaurant.dto.guestbook.GuestbookRequestDto;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guestbook")
@Tag(name = "Guestbook API", description = "방명록 관련 API")
public class GuestbookController {

    private final GuestbookService guestbookService;
    private final ImageService imageService;

    @PostMapping("/{restaurantId}")
    @Operation(summary = "방명록 생성", description = "해당 맛집의 새로운 방명록을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> createGuestbook(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId,
                                             @RequestBody @Parameter(description = "방명록 생성 요청 DTO", required = true) GuestbookRequestDto requestDto) {
        guestbookService.createGuestbook(userDetails.getUsername(), restaurantId, requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }
}
