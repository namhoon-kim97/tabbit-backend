package com.jungle.Tabbit.domain.stampBadge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "시도 스탬프")
public class SidoStampResponseDto {
    @Schema(description = "시도")
    private String sidoName;
    @Schema(description = "해당 시도 총 스탬프 수", example = "10")
    private Long totalsidoStampCount;
    @Schema(description = "해당 시도 획득 스탬프 수", example = "5")
    private Long earnedsidoStampCount;

    public static SidoStampResponseDto of(String sidoName, Long totalsidoStampCount, Long earnedsidoStampCount) {
        return SidoStampResponseDto.builder()
                .sidoName(sidoName)
                .totalsidoStampCount(totalsidoStampCount)
                .earnedsidoStampCount(earnedsidoStampCount)
                .build();
    }
}