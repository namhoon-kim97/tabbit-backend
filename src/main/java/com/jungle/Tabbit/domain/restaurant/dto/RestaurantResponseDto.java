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
    private String categoryName; // 카테고리 이름
    private String summaryAddress; // 요약 주소
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도
    @JsonProperty("isEarnedStamp")
    private boolean isEarnedStamp; // 스탬프 획득 유무

    public static RestaurantResponseDto of(Restaurant restaurant, boolean isEarnedStamp) {
        return RestaurantResponseDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .categoryName(restaurant.getCategory().getCategoryName())
                .summaryAddress(restaurant.getAddress().getSido() + " " + restaurant.getAddress().getSigungu() + " " + restaurant.getAddress().getEupmyeondong())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .isEarnedStamp(isEarnedStamp)
                .build();
    }
}
