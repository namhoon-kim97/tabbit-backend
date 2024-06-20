package com.jungle.Tabbit.domain.restaurant.controller;

import com.jungle.Tabbit.domain.restaurant.dto.RestaurantCreateRequestDto;
import com.jungle.Tabbit.domain.restaurant.service.RestaurantService;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public CommonResponse<?> getRestaurantAll() {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, restaurantService.getAllRestaurant());
    }

    @PostMapping
    public CommonResponse<?> createRestaurant(@RequestBody RestaurantCreateRequestDto restaurantCreateRequestDto) {
        restaurantService.createRestaurant(restaurantCreateRequestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }
}
