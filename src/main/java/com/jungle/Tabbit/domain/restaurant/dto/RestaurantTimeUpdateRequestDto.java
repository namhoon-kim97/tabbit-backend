package com.jungle.Tabbit.domain.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "맛집 예상대기시간 변경 요청 DTO")
public class RestaurantTimeUpdateRequestDto {
    @Schema(description = "팀당 예상 대기 시간")
    private Long estimatedTimePerTeam; // 팀당 예상 대기 시간
}