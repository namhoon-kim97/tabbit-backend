package com.jungle.Tabbit.domain.stampBadge.dto;

import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "칭호 응답 DTO")
public class BadgeResponseDto {
    @Schema(description = "칭호 id", example = "1")
    private Long badgeId;
    @Schema(description = "칭호 이름", example = "한식 러버")
    private String name;
    @Schema(description = "칭호 설명", example = "한식을 5번 이상 드셨어요. 역시 한국인은 밥심!")
    private String description;
    @Schema(description = "칭호 획득 유무", example = "true")
    private Boolean earnedBadge; // 칭호 획득 유무

    public static BadgeResponseDto of(Badge badge, Boolean earnedBadge) {
        return BadgeResponseDto.builder()
                .badgeId(badge.getBadgeId())
                .name(badge.getName())
                .description(badge.getDescription())
                .earnedBadge(earnedBadge)
                .build();
    }
}