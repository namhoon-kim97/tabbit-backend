package com.jungle.Tabbit.domain.restaurant.controller;

import com.jungle.Tabbit.domain.restaurant.dto.RestaurantCreateRequestDto;
import com.jungle.Tabbit.domain.restaurant.service.RestaurantService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public CommonResponse<?> getRestaurantAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, restaurantService.getAllRestaurant(userDetails.getUsername()));
    }

    @PostMapping
    public CommonResponse<?> createRestaurant(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RestaurantCreateRequestDto requestDto) {
        restaurantService.createRestaurant(requestDto, userDetails.getUsername());
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }

    @GetMapping("/{restaurantId}")
    public CommonResponse<?> getRestaurantSummaryById(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long restaurantId) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, restaurantService.getRestaurantSummaryInfo(restaurantId, userDetails.getUsername()));
    }
}
