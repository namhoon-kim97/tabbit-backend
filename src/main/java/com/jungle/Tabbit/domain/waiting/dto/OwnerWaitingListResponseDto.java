package com.jungle.Tabbit.domain.waiting.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OwnerWaitingListResponseDto {
    private List<WaitingResponseDto> calledWaitingList;
    private List<WaitingResponseDto> waitingList;

    public OwnerWaitingListResponseDto(List<WaitingResponseDto> calledWaitingList, List<WaitingResponseDto> waitingList) {
        this.calledWaitingList = calledWaitingList;
        this.waitingList = waitingList;
    }
}
