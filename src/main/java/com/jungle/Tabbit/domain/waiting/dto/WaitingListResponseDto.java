package com.jungle.Tabbit.domain.waiting.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WaitingListResponseDto {
    private List<WaitingResponseDto> waitingResponseDtos;
}
