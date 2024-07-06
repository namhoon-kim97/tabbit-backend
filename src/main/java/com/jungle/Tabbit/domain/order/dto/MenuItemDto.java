package com.jungle.Tabbit.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuItemDto {
    private Long menuId;
    private String menuName;
    private int quantity;
    private Long price;
}
