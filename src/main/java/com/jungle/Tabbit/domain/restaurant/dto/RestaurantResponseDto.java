package com.jungle.Tabbit.domain.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class RestaurantResponseDto {

    private Long restaurantId;
    private String name; // 가게 이름
    private String category_name; // 카테고리 이름
    private String summary_address; // 요약 주소
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도
    @JsonProperty("is_earned_stamp")
    private boolean is_earned_stamp; // 스탬프 획득 유무

    public static RestaurantResponseDto of(Restaurant restaurant, boolean is_earned_stamp) {
        return RestaurantResponseDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .category_name(restaurant.getCategory().getCategoryName())
                .summary_address(restaurant.getAddress().getSido() + " " + restaurant.getAddress().getSigungu() + " " + restaurant.getAddress().getEupmyeondong())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .is_earned_stamp(is_earned_stamp)
                .build();
    }
}
