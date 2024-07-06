package com.jungle.Tabbit.domain.order.dto.order;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    private Long memberId;
    private Long restaurantId;
    private List<MenuQuantityDto> menuQuantities;
}
