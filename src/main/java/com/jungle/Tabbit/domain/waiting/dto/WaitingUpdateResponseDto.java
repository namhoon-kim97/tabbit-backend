package com.jungle.Tabbit.domain.waiting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "웨이팅 업데이트 응답 DTO")
public class WaitingUpdateResponseDto {

    @Schema(description = "웨이팅 ID", example = "1")
    private Long waitingId;

    @Schema(description = "식사 인원", example = "4")
    private int peopleNumber;

    @Schema(description = "대기 번호", example = "10")
    private int waitingNumber;

    @Schema(description = "웨이팅 상태", example = "STATUS_WAITING")
    private WaitingStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "생성 일자", example = "2023-06-21 14:30:00")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "수정 일자", example = "2023-06-21 14:45:00")
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
