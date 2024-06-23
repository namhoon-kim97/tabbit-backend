package com.jungle.Tabbit.domain.waiting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WaitingUpdateResponseDto {
    private Long waitingId;
    private int peopleNumber; // 식사 인원
    private int waitingNumber; // 대기 번호
    private WaitingStatus status; // 상태

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static WaitingUpdateResponseDto of(Waiting waiting) {
        return WaitingUpdateResponseDto.builder()
                .waitingId(waiting.getWaitingId())
                .peopleNumber(waiting.getPeopleNumber())
                .waitingNumber(waiting.getWaitingNumber())
                .status(waiting.getWaitingStatus())
                .createdAt(waiting.getCreatedAt())
                .updatedAt(waiting.getUpdatedAt())
                .build();
    }
}
