package com.jungle.Tabbit.domain.restaurant.dto;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.entity.RestaurantDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "맛집 디테일 응답 DTO")
public class RestaurantResponseDetailDto {

    @Schema(description = "맛집 id", example = "1")
    private Long restaurantId;

    @Schema(description = "가게 이름", example = "맛있는 식당")
    private String name; // 가게 이름

    @Schema(description = "맛집 이미지 경로", example = "image.png")
    private String imageUrl; // 맛집 이미지 경로

    @Schema(description = "카테고리 이름", example = "한식")
    private String categoryName; // 카테고리 이름

    @Schema(description = "도로명 주소", example = "서울특별시 강남구 테헤란로 123")
    private String roadAddress; // 도로명 주소

    @Schema(description = "상세 주소", example = "2층")
    private String detailAddress; // 상세 주소

    @Schema(description = "운영 시간", example = "10:00 ~ 22:00")
    private String openingHours; // 운영 시간

    @Schema(description = "브레이크 타임", example = "15:00 ~ 17:00")
    private String breakTime; // 브레이크 타임

    @Schema(description = "휴무일", example = "매주 월요일")
    private String holidays; // 휴무일

    @Schema(description = "전화번호", example = "02-1234-5678")
    private String restaurantNumber; // 전화번호

    @Schema(description = "매장소개", example = "이 곳은 맛있는 음식을 제공하는 식당입니다.")
    private String description; // 매장소개

    @Schema(description = "스탬프 획득 유무", example = "true")
    private Boolean earnedStamp; // 스탬프 획득 유무

    public static RestaurantResponseDetailDto of(Restaurant restaurant, RestaurantDetail restaurantDetail, Boolean earnedStamp) {
        return RestaurantResponseDetailDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .imageUrl(restaurant.getImageUrl())
                .categoryName(restaurant.getCategory().getCategoryName())
                .roadAddress(restaurant.getAddress().getRoadAddress())
                .detailAddress(restaurant.getAddress().getDetailAddress())
                .openingHours(restaurantDetail.getOpeningHours())
                .breakTime(restaurantDetail.getBreakTime())
                .holidays(restaurantDetail.getHolidays())
                .restaurantNumber(restaurantDetail.getRestaurantNumber())
                .description(restaurantDetail.getDescription())
                .earnedStamp(earnedStamp)
                .build();
    }
}
