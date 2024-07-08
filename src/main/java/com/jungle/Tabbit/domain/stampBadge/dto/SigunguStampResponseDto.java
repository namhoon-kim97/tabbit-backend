package com.jungle.Tabbit.domain.stampBadge.dto;

import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "시군구 스탬프")
public class SigunguStampResponseDto {
    @Schema(description = "시군구")
    private String sigunguName;
    @Schema(description = "해당 시군구 총 스탬프 수", example = "10")
    private Long totalsigunguStampCount;
    @Schema(description = "해당 시군구 획득 스탬프 수", example = "5")
    private Long earnedsigunguStampCount;
    @Schema(description = "해당 시군구 맛집 리스트")
    private List<RestaurantResponseDto> restaurantList;

    public static SigunguStampResponseDto of(String sigunguName, Long totalsigunguStampCount, Long earnedsigunguStampCount, List<RestaurantResponseDto> restaurantList) {
        return SigunguStampResponseDto.builder()
                .sigunguName(sigunguName)
                .totalsigunguStampCount(totalsigunguStampCount)
                .earnedsigunguStampCount(earnedsigunguStampCount)
                .restaurantList(restaurantList)
                .build();
    }
}