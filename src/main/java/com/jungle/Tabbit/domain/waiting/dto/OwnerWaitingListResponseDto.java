package com.jungle.Tabbit.domain.waiting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "맛집의 웨이팅 목록 점주용 응답 DTO")
public class OwnerWaitingListResponseDto {

    @Schema(description = "호출된 웨이팅 목록")
    private List<WaitingUpdateResponseDto> calledWaitingList;

    @Schema(description = "대기 중인 웨이팅 목록")
    private List<WaitingUpdateResponseDto> waitingList;

    public OwnerWaitingListResponseDto(List<WaitingUpdateResponseDto> calledWaitingList, List<WaitingUpdateResponseDto> waitingList) {
        this.calledWaitingList = calledWaitingList;
        this.waitingList = waitingList;
    }
}