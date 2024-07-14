package com.jungle.Tabbit.domain.order.dto.order;


import com.jungle.Tabbit.domain.order.entity.Order;
import com.jungle.Tabbit.domain.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Schema(description = "주문 조회 응답 DTO")
public class OrderResponseDto {
    @Schema(description = "주문 ID")
    private Long orderId;
    @Schema(description = "유저 ID")
    private Long memberId;
    @Schema(description = "가게 ID")
    private Long restaurantId;
    @Schema(description = "주문 상태")
    private OrderStatus status;
    @Schema(description = "메뉴 리스트")
    private List<OrderMenuResponseDto> menuItems;

    public static OrderResponseDto of(Order order) {
        List<OrderMenuResponseDto> menuItems = order.getOrderMenus() != null ?
                order.getOrderMenus().stream()
                        .map(orderItem -> OrderMenuResponseDto.builder()
                                .menuId(orderItem.getMenu().getMenuId())
                                .menuName(orderItem.getMenu().getName())
                                .quantity(orderItem.getQuantity())
                                .price(orderItem.getMenu().getPrice())
                                .imageUrl(orderItem.getMenu().getImageUrl())
                                .build())
                        .collect(Collectors.toList())
                : Collections.emptyList();

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .memberId(order.getMember().getMemberId())
                .restaurantId(order.getRestaurant().getRestaurantId())
                .status(order.getStatus())
                .menuItems(menuItems)
                .build();
    }
}
