package com.jungle.Tabbit.domain.waiting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "맛집의 웨이팅 목록 응답 DTO")
public class WaitingListResponseDto {
    private List<WaitingResponseDto> waitingResponseDtos;
}
