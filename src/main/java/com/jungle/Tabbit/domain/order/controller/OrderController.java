package com.jungle.Tabbit.domain.order.controller;

import com.jungle.Tabbit.domain.order.dto.OrderRequestDto;
import com.jungle.Tabbit.domain.order.service.OrderService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public CommonResponse<?> createOrder(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody OrderRequestDto requestDto) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, orderService.createOrder(userDetails.getUsername(), requestDto));
    }
}
