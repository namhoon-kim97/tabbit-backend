package com.jungle.Tabbit.domain.stampBadge.dto;

import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StampResponseDto {
    private String sidoName; // 시도명
    private Long totalStampCount; // 해당 지역 총 스탬프 수
    private Long earnedStampCount; // 해당 지역 획득 스탬프 수
    private List<RestaurantResponseDto> restaurantList; // 해당 지역 맛집 리스트

    public static StampResponseDto of(String sidoName, Long totalStampCount, Long earnedStampCount, List<RestaurantResponseDto> restaurantList) {
        return StampResponseDto.builder()
                .sidoName(sidoName)
                .totalStampCount(totalStampCount)
                .earnedStampCount(earnedStampCount)
                .restaurantList(restaurantList)
                .build();
    }
}