package com.jungle.Tabbit.domain.order.dto;


import com.jungle.Tabbit.domain.order.entity.Order;
import com.jungle.Tabbit.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderResponseDto {
    private Long orderId;
    private Long memberId;
    private Long restaurantId;
    private OrderStatus status;
    private List<MenuItemDto> menuItems;

    public static OrderResponseDto of(Order order) {
        List<MenuItemDto> menuItems = order.getOrderMenus() != null ?
                order.getOrderMenus().stream()
                        .map(orderItem -> MenuItemDto.builder()
                                .menuId(orderItem.getMenu().getMenuId())
                                .menuName(orderItem.getMenu().getName())
                                .quantity(orderItem.getQuantity())
                                .price(orderItem.getMenu().getPrice())
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
