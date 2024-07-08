package com.jungle.Tabbit.domain.restaurant.dto;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RestaurantResponseSearchDto {
    @Schema(description = "맛집 id", example = "1")
    private Long restaurantId;

    @Schema(description = "가게 이름", example = "맛있는 식당")
    private String name;

    @Schema(description = "맛집 이미지 경로", example = "image.png")
    private String imageUrl;

    @Schema(description = "카테고리 이름", example = "한식")
    private String category;

    @Schema(description = "도로명 주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;

    @Schema(description = "위도", example = "37.5665")
    private String latitude;

    @Schema(description = "경도", example = "126.9780")
    private String longitude;

    @Schema(description = "스탬프 획득 유무", example = "true")
    private Boolean earnedStamp;

    public RestaurantResponseSearchDto(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.name = restaurant.getName();
        this.imageUrl = restaurant.getImageUrl();
        this.category = restaurant.getCategory().getCategoryName();
        this.address = restaurant.getAddress().getRoadAddress();  // 필요에 따라 주소 필드 구성
        this.latitude = restaurant.getLatitude().toString();
        this.longitude = restaurant.getLongitude().toString();
    }
}
