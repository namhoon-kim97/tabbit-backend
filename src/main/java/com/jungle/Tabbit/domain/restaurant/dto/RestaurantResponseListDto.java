package com.jungle.Tabbit.domain.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "맛집리스트 응답 DTO")
public class RestaurantResponseListDto {
    @Schema(description = "맛집 리스트")
    private List<RestaurantResponseDto> restaurantResponseList;
}
