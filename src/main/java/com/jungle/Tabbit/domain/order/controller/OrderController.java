package com.jungle.Tabbit.domain.order.controller;

import com.jungle.Tabbit.domain.order.dto.order.OrderCreateResponseDto;
import com.jungle.Tabbit.domain.order.dto.order.OrderRequestDto;
import com.jungle.Tabbit.domain.order.dto.order.OrderResponseDto;
import com.jungle.Tabbit.domain.order.dto.order.OrderUpdateRequestDto;
import com.jungle.Tabbit.domain.order.service.OrderService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "주문 관련 API")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = OrderCreateResponseDto.class)))
    public CommonResponse<?> createOrder(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody OrderRequestDto requestDto) {

        return CommonResponse.success(ResponseStatus.SUCCESS_OK, orderService.createOrder(userDetails.getUsername(), requestDto));
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "주문 수정", description = "해당 주문을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> updateOrder(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long orderId, @RequestBody OrderUpdateRequestDto requestDto) {
        orderService.updateOrder(userDetails.getUsername(), requestDto, orderId);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "유저 주문 조회", description = "특정 유저의 주문을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = OrderResponseDto.class)))
    public CommonResponse<?> getUserOrders(@PathVariable Long orderId) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, orderService.getUserOrders(orderId));
    }
}
