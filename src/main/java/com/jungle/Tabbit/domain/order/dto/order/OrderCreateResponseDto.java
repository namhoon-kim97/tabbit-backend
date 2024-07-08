package com.jungle.Tabbit.domain.order.dto.order;

import com.jungle.Tabbit.domain.order.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponseDto {
    @Schema(description = "주문 ID")
    private Long orderId;

    public static OrderCreateResponseDto of(Order order) {
        return OrderCreateResponseDto.builder()
                .orderId(order.getOrderId())
                .build();
    }
}
