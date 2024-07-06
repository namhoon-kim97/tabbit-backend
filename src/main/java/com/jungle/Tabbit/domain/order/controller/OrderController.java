package com.jungle.Tabbit.domain.order.controller;

import com.jungle.Tabbit.domain.order.dto.order.OrderRequestDto;
import com.jungle.Tabbit.domain.order.service.OrderService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public CommonResponse<?> createOrder(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody OrderRequestDto requestDto) {
        orderService.createOrder(userDetails.getUsername(), requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
    }

    @GetMapping("/{restaurantId}")
    public CommonResponse<?> getAllOrders(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long restaurantId) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, orderService.getAllOrders(userDetails.getUsername(), restaurantId));
    }
}
