package com.jungle.Tabbit.domain.restaurant.dto;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class RestaurantResponseDto {

    private Long restaurantId;
    private String name; // 가게 이름
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도
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