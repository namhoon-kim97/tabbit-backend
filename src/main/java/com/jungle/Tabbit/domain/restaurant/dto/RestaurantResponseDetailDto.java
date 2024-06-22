package com.jungle.Tabbit.domain.restaurant.dto;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantResponseDetailDto {

    private Long restaurantId;
    private String name; // 가게 이름
    private String categoryName; // 카테고리 이름
    private String roadAddress; // 도로명 주소
    private String detailAddress; // 상세 주소
    private String openingHours; // 운영 시간
    private String breakTime; // 브레이크 타임
    private String holidays; // 휴무일
    private String restaurantNumber; // 전화번호
    private String description; // 매장소개
    private boolean earnedStamp; // 스탬프 획득 유무

    public static RestaurantResponseDetailDto of(Restaurant restaurant, boolean earnedStamp) {
        return RestaurantResponseDetailDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .categoryName(restaurant.getCategory().getCategoryName())
                .roadAddress(restaurant.getAddress().getRoadAddress())
                .detailAddress(restaurant.getAddress().getDetailAddress())
                .openingHours(restaurant.getRestaurantDetail().getOpeningHours())
                .breakTime(restaurant.getRestaurantDetail().getBreakTime())
                .holidays(restaurant.getRestaurantDetail().getHolidays())
                .restaurantNumber(restaurant.getRestaurantDetail().getRestaurantNumber())
                .description(restaurant.getRestaurantDetail().getDescription())
                .earnedStamp(earnedStamp)
                .build();
    }
}