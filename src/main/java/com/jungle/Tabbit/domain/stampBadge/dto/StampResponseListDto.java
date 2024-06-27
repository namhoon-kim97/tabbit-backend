package com.jungle.Tabbit.domain.stampBadge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "스탬프 응답 리스트 DTO")
public class StampResponseListDto {
    @Schema(description = "총 스탬프 수", example = "100")
    private Long totalStampCount;
    @Schema(description = "획득 스탬프 수", example = "50")
    private Long earnedStampCount;
    @Schema(description = "스탬프 응답 리스트")
    private List<StampResponseDto> stampResponseList;

    public static StampResponseListDto of(Long totalStampCount, Long earnedStampCount, List<StampResponseDto> stampResponseList) {
        return StampResponseListDto.builder()
                .totalStampCount(totalStampCount)
                .earnedStampCount(earnedStampCount)
                .stampResponseList(stampResponseList)
                .build();
    }
}