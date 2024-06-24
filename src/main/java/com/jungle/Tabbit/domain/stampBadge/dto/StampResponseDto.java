package com.jungle.Tabbit.domain.stampBadge.dto;

import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "스탬프 응답 DTO")
public class StampResponseDto {
    @Schema(description = "시도명", example = "서울특별시")
    private String sidoName;
    @Schema(description = "해당 지역 총 스탬프 수", example = "10")
    private Long totalStampCount;
    @Schema(description = "해당 지역 획득 스탬프 수", example = "5")
    private Long earnedStampCount;
    @Schema(description = "해당 지역 맛집 리스트")
    private List<RestaurantResponseDto> restaurantList;

    public static StampResponseDto of(String sidoName, Long totalStampCount, Long earnedStampCount, List<RestaurantResponseDto> restaurantList) {
        return StampResponseDto.builder()
                .sidoName(sidoName)
                .totalStampCount(totalStampCount)
                .earnedStampCount(earnedStampCount)
                .restaurantList(restaurantList)
                .build();
    }
}