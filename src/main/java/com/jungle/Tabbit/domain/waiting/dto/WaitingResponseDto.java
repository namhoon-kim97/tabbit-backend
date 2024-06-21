package com.jungle.Tabbit.domain.waiting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WaitingResponseDto {
    private Long waitingId;
    private int peopleNumber; // 식사 인원
    private int waitingNumber; // 대기 번호
    private WaitingStatus status; // 상태
    private Long estimatedWaitTime; // 예상 대기 시간
    private int currentWaitingPosition; // 현재 나의 순서

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static WaitingResponseDto of(Waiting waiting, Long estimatedWaitTime, int currentWaitingPosition) {
        return WaitingResponseDto.builder()
                .waitingId(waiting.getWaitingId())
                .peopleNumber(waiting.getPeopleNumber())
                .waitingNumber(waiting.getWaitingNumber())
                .status(waiting.getWaitingStatus())
                .estimatedWaitTime(estimatedWaitTime)
                .currentWaitingPosition(currentWaitingPosition)
                .createdAt(waiting.getCreatedAt())
                .updatedAt(waiting.getUpdatedAt())
                .build();
    }
}