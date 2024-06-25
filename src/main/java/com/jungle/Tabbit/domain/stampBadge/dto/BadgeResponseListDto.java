package com.jungle.Tabbit.domain.stampBadge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "칭호 응답 리스트 DTO")
public class BadgeResponseListDto {
    @Schema(description = "칭호 응답 리스트")
    private List<BadgeResponseDto> badgeResponseList;
}