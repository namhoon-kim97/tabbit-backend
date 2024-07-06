package com.jungle.Tabbit.domain.waiting.dto;

import com.jungle.Tabbit.domain.order.dto.order.OrderResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "웨이팅과 관련된 주문 목록을 포함한 DTO")
public class WaitingWithOrderDto {
    @Schema(description = "웨이팅 목록")
    private WaitingUpdateResponseDto waiting;
    @Schema(description = "주문 목록")
    private List<OrderResponseDto> orders;
}
