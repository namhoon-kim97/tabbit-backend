package com.jungle.Tabbit.domain.waiting.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OwnerWaitingListResponseDto {
    private List<WaitingUpdateResponseDto> calledWaitingList;
    private List<WaitingUpdateResponseDto> waitingList;

    public OwnerWaitingListResponseDto(List<WaitingUpdateResponseDto> calledWaitingList, List<WaitingUpdateResponseDto> waitingList) {
        this.calledWaitingList = calledWaitingList;
        this.waitingList = waitingList;
    }
}
