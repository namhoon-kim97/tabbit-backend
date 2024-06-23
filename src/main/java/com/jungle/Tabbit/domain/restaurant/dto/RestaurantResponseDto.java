package com.jungle.Tabbit.domain.restaurant.dto;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@Schema(description = "맛집 응답 DTO")
public class RestaurantResponseDto {

    @Schema(description = "맛집 id", example = "1")
    private Long restaurantId;

    @Schema(description = "가게 이름", example = "맛있는 식당")
    private String name; // 가게 이름

    @Schema(description = "위도", example = "37.5665")
    private BigDecimal latitude; // 위도

    @Schema(description = "경도", example = "126.9780")
    private BigDecimal longitude; // 경도

    @Schema(description = "스탬프 획득 유무", example = "true")
    private Boolean earnedStamp; // 스탬프 획득 유무

    public static RestaurantResponseDto of(Restaurant restaurant, Boolean earnedStamp) {
        return RestaurantResponseDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .earnedStamp(earnedStamp)
                .build();
    }
}
