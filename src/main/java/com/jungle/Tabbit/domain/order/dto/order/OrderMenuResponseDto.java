package com.jungle.Tabbit.domain.order.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "주문 메뉴 응답 DTO")
public class OrderMenuResponseDto {
    @Schema(description = "메뉴 ID")
    private Long menuId;
    @Schema(description = "메뉴 이름")
    private String menuName;
    @Schema(description = "메뉴 수량")
    private int quantity;
    @Schema(description = "메뉴 가격")
    private Long price;
}
