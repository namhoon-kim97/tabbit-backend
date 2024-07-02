package com.jungle.Tabbit.domain.restaurant.dto;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "맛집 요약 응답 DTO")
public class RestaurantResponseSummaryDto {

    @Schema(description = "맛집 id", example = "1")
    private Long restaurantId;

    @Schema(description = "맛집 이름", example = "맛있는 식당")
    private String name; // 맛집 이름

    @Schema(description = "맛집 이미지 경로", example = "image.png")
    private String imageUrl; // 맛집 이미지 경로

    @Schema(description = "카테고리 이름", example = "한식")
    private String categoryName; // 카테고리 이름

    @Schema(description = "요약 주소", example = "서울특별시 강남구 역삼동")
    private String summaryAddress; // 요약 주소

    @Schema(description = "스탬프 획득 유무", example = "true")
    private Boolean earnedStamp; // 스탬프 획득 유무

    @Schema(description = "현재 대기팀 수", example = "5")
    private Long currentWaitingNumber; // 현재 대기팀 수

    @Schema(description = "예상 대기시간", example = "10")
    private Long estimatedWaitTime; // 예상 대기시간

    public static RestaurantResponseSummaryDto of(Restaurant restaurant, Boolean earnedStamp, Long currentWaitingNumber, Long estimatedWaitTime) {
        return RestaurantResponseSummaryDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .imageUrl(restaurant.getImageUrl())
                .categoryName(restaurant.getCategory().getCategoryName())
                .summaryAddress(restaurant.getAddress().getSido() + " " + restaurant.getAddress().getSigungu() + " " + restaurant.getAddress().getEupmyeondong())
                .earnedStamp(earnedStamp)
                .currentWaitingNumber(currentWaitingNumber)
                .estimatedWaitTime(estimatedWaitTime)
                .build();
    }
}
