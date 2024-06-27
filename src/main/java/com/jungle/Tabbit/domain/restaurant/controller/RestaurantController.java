package com.jungle.Tabbit.domain.restaurant.controller;

import com.jungle.Tabbit.domain.restaurant.dto.RestaurantCreateRequestDto;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseDetailDto;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseListDto;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseSummaryDto;
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

    @PostMapping
    @Operation(summary = "맛집 생성", description = "새로운 맛집을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> createRestaurant(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Parameter(description = "맛집 생성 요청 DTO", required = true) RestaurantCreateRequestDto requestDto) {
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
}
