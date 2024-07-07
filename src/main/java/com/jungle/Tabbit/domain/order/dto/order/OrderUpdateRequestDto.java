package com.jungle.Tabbit.domain.order.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "주문 수정 요청 DTO")
public class OrderUpdateRequestDto {
    private List<MenuQuantityDto> menuQuantities;
}
