package com.jungle.Tabbit.domain.stampBadge.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StampResponseListDto {
    private List<StampResponseDto> stampResponseList;
}
