package com.jungle.Tabbit.domain.restaurant.dto;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class RestaurantResponseDto {

    private Long restaurantId;

    private String name;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private boolean is_stamp;

    public static RestaurantResponseDto of(Restaurant restaurant, boolean is_stamp) {
        return RestaurantResponseDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .is_stamp(is_stamp)
                .build();
    }
}
