package com.jungle.Tabbit.domain.order.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "주문 요청 DTO")
public class OrderRequestDto {
    @Schema(description = "웨이팅 ID", example = "5")
    private Long waitingId;
    @Schema(description = "가게 ID")
    private Long restaurantId;
    @Schema(description = "메뉴 리스트")
    private List<MenuQuantityDto> menuQuantities;
}
