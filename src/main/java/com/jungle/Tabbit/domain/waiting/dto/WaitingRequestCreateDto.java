package com.jungle.Tabbit.domain.waiting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "웨이팅 생성 요청 DTO")
public class WaitingRequestCreateDto {

    @Schema(description = "식사 인원 수", example = "4")
    private int peopleNumber;

    @Schema(description = "NFC ID", example = "nfc123456")
    private String nfcId;
}
