package com.jungle.Tabbit.domain.restaurant.controller;

import com.jungle.Tabbit.domain.restaurant.service.RestaurantService;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
