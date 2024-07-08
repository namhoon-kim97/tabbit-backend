package com.jungle.Tabbit.domain.stampBadge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "스탬프 리스트 DTO")
public class StampListDto {
    @Schema(description = "시도명")
    private String sidoName;
    @Schema(description = "시군구 스탬프 리스트")
    private List<SigunguStamp> sigunguStampList;

    public static StampListDto of(String sidoName, List<SigunguStamp> sigunguStampList) {
        return StampListDto.builder()
                .sidoName(sidoName)
                .sigunguStampList(sigunguStampList)
                .build();
    }
}