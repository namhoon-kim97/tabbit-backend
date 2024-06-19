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
    private int peopleNumber;
    private int waitingNumber;
    private WaitingStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static WaitingResponseDto of(Waiting waiting) {
        return WaitingResponseDto.builder()
                .waitingId(waiting.getId())
                .peopleNumber(waiting.getPeopleNumber())
                .waitingNumber(waiting.getWaitingNumber())
                .status(waiting.getWaitingStatus())
                .createdAt(waiting.getCreatedAt())
                .updatedAt(waiting.getUpdatedAt())
                .build();
    }
}
