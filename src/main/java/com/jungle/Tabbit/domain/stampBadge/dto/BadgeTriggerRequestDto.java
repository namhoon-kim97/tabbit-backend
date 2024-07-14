package com.jungle.Tabbit.domain.stampBadge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "칭호 트리거 요청 DTO")
public class BadgeTriggerRequestDto {
    @Schema(description = "Badge ID", example = "1")
    private Long badgeId;
    @Schema(description = "카테고리 코드", example = "CHK")
    private String categoryCd;
    @Schema(description = "필요 스탬프 개수", example = "1")
    private Long requiredStampCnt;
    @Schema(description = "트리거 타입", example = "TOTAL_STAMPS")
    private String triggerType;
    @Schema(description = "트리거 조건", example = "30")
    private String condition;

    public BadgeTriggerRequestDto(Long badgeId, String categoryCd, Long requiredStampCnt, String triggerType, String condition) {
        this.badgeId = badgeId;
        this.categoryCd = categoryCd;
        this.requiredStampCnt = requiredStampCnt;
        this.triggerType = triggerType;
        this.condition = condition;
    }
}
