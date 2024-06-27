package com.jungle.Tabbit.domain.stampBadge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "칭호 응답 리스트 DTO")
public class BadgeResponseListDto {
    @Schema(description = "총 칭호 수", example = "5")
    private Long totalBadgeCount;
    @Schema(description = "획득 칭호 수", example = "1")
    private Long earnedBadgeCount;
    @Schema(description = "칭호 응답 리스트")
    private List<BadgeResponseDto> badgeResponseList;

    public static BadgeResponseListDto of(Long totalBadgeCount, Long earnedBadgeCount, List<BadgeResponseDto> badgeResponseList) {
        return BadgeResponseListDto.builder()
                .totalBadgeCount(totalBadgeCount)
                .earnedBadgeCount(earnedBadgeCount)
                .badgeResponseList(badgeResponseList)
                .build();
    }
}