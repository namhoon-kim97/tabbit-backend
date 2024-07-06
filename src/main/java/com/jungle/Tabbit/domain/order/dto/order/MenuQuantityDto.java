package com.jungle.Tabbit.domain.order.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "메뉴 수량 요청 DTO")
public class MenuQuantityDto {
    @Schema(description = "메뉴 ID")
    private Long menuId;
    @Schema(description = "메뉴 수량")
    private int quantity;
}
