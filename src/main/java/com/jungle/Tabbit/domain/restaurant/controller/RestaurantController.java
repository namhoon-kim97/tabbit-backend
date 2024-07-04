package com.jungle.Tabbit.domain.restaurant.controller;

import com.jungle.Tabbit.domain.restaurant.dto.*;
import com.jungle.Tabbit.domain.restaurant.service.RestaurantService;
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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurant API", description = "맛집 관련 API")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "모든 맛집 조회", description = "모든 맛집 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = RestaurantResponseListDto.class)))
    public CommonResponse<?> getRestaurantAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        RestaurantResponseListDto responseDto = restaurantService.getAllRestaurant(userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @GetMapping("/owner")
    @Operation(summary = "점주 맛집 조회", description = "점주 소유의 맛집의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = RestaurantResponseListDto.class)))
    public CommonResponse<?> getOwnerRestaurantAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        RestaurantResponseListDto responseDto = restaurantService.getAllOwnerRestaurant(userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "맛집 생성", description = "새로운 맛집을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> createRestaurant(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @Parameter(description = "맛집 생성 요청 DTO / 이미지 파일 없을 시 DEFAULT", required = true) RestaurantRequestDto requestDto) {
        restaurantService.createRestaurant(requestDto, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }

    @GetMapping("/{restaurantId}")
    @Operation(summary = "맛집 요약 정보 조회", description = "특정 맛집의 요약 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = RestaurantResponseSummaryDto.class)))
    public CommonResponse<?> getRestaurantSummary(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId) {
        RestaurantResponseSummaryDto responseDto = restaurantService.getRestaurantSummaryInfo(restaurantId, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @GetMapping("/detail/{restaurantId}")
    @Operation(summary = "맛집 상세 정보 조회", description = "특정 맛집의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = RestaurantResponseDetailDto.class)))
    public CommonResponse<?> getRestaurantDetail(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId) {
        RestaurantResponseDetailDto responseDto = restaurantService.getRestaurantDetailInfo(restaurantId, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @PutMapping("time/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "맛집 예상 대기 시간 변경", description = "점주가 맛집의 예상 대기 시간을 변경합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> updateRestaurantEstimatedTime(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId,
                                                           @RequestBody @Parameter(description = "예상 대기 시간 변경 요청 DTO", required = true) RestaurantTimeUpdateRequestDto requestDto) {
        restaurantService.updateRestaurantEstimatedTime(restaurantId, requestDto, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
    }

    @PutMapping(value = "/{restaurantId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "맛집 정보 변경", description = "점주가 맛집 정보를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> updateRestaurant(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId,
                                              @Parameter(description = "맛집 정보 변경 요청 DTO", required = true) RestaurantRequestDto requestDto) {
        restaurantService.updateRestaurant(restaurantId, requestDto, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
    }
}
