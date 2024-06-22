package com.jungle.Tabbit.domain.restaurant.dto;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantResponseSummaryDto {

    private Long restaurantId;
    private String name; // 가게 이름
    private String categoryName; // 카테고리 이름
    private String summaryAddress; // 요약 주소
    private boolean earnedStamp; // 스탬프 획득 유무
    private Long currentWaitingNumber; // 현재 대기팀 수

    public static RestaurantResponseSummaryDto of(Restaurant restaurant, boolean earnedStamp, Long currentWaitingNumber) {
        return RestaurantResponseSummaryDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .categoryName(restaurant.getCategory().getCategoryName())
                .summaryAddress(restaurant.getAddress().getSido() + " " + restaurant.getAddress().getSigungu() + " " + restaurant.getAddress().getEupmyeondong())
                .earnedStamp(earnedStamp)
                .currentWaitingNumber(currentWaitingNumber)
                .build();
    }
}